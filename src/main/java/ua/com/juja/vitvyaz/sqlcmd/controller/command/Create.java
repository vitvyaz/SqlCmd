package ua.com.juja.vitvyaz.sqlcmd.controller.command;

import ua.com.juja.vitvyaz.sqlcmd.controller.command.util.InputLine;
import ua.com.juja.vitvyaz.sqlcmd.model.DatabaseManager;
import ua.com.juja.vitvyaz.sqlcmd.view.View;

/**
 * Created by Vitalii Viazovoi on 23.04.2016.
 */
public class Create extends Command {

    public Create(View view, DatabaseManager dbManager) {
        super(view, dbManager);
        description = "\tcreate tableName ( columnName1 dataType1 [PRIMARY KEY] [NOT NULL]," +
                " ... columnNameN dataTypeN [NOT NULL] )\n" +
                "\t\tсоздать таблицу";
        formats = null;
    }

    @Override
    public boolean canProcess(InputLine line) {
        return line.getWord(0).equals("create");
    }

    @Override
    public void process(InputLine line) {
        StringBuilder query = new StringBuilder();
        for (int i = 1; i < line.countWords(); i++) {
            query.append(line.getWord(i) + " ");
        }
        dbManager.createTable(query.toString());
        String tableName = line.getWord(1);
        view.write(String.format("Таблица %s создана", tableName));
    }
}
