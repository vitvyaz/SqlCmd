package ua.com.juja.vitvyaz.sqlcmd.controller.command;

import ua.com.juja.vitvyaz.sqlcmd.controller.command.util.InputLine;
import ua.com.juja.vitvyaz.sqlcmd.model.DataSet;
import ua.com.juja.vitvyaz.sqlcmd.model.DatabaseManager;
import ua.com.juja.vitvyaz.sqlcmd.model.TableBuilder;
import ua.com.juja.vitvyaz.sqlcmd.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vitalii Viazovoi on 12.04.2016.
 */
public class Find extends Command {

    public Find(View view, DatabaseManager dbManager) {
        super(view, dbManager);
        description = "\tfind tableName [LIMIT OFFET]\n" +
                "\t\tвывести содержимое таблицы [LIMIT - количество строк OFFSET - начальная строка]";
        formats = new String[] {"find tableName", "find tableName LIMIT OFFET"};
    }

    @Override
    public boolean canProcess(InputLine line) {
        return line.getWord(0).equals("find");
    }

    @Override
    public void process(InputLine line) {
        if (formats != null) {
            line.parametersNumberValidation(formats);
        }

        String tableName = line.getWord(1);
        line.tableNameValidation(dbManager, tableName);

        List<DataSet> tableData = new ArrayList<>();
        if (line.countWords() == 2) {
            tableData = dbManager.getTableData(tableName);
        } else if (line.countWords() == 4) {
            int limit = Integer.valueOf(line.getWord(2));
            int offset = Integer.valueOf(line.getWord(3));
            tableData = dbManager.getTableData(tableName, limit, offset);
        }
        view.write(new TableBuilder(dbManager.getTableColumns(tableName), tableData).toString());
    }
}
