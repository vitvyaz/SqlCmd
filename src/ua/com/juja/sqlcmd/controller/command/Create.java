package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.command.util.InputLine;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Vitalii Viazovoi on 23.04.2016.
 */
public class Create extends Command {

    public Create(View view, DatabaseManager dbManager) {
        super(view, dbManager);
        description = "\tcreate tableName ( columnName1 dataType1 [PRIMARY KEY] [NOT NULL], ... comumnNameN dataTypeN [NOT NULL] )" +
                "\t\tсоздать таблицу";
        formats = null;
    }

    @Override
    public boolean canProcess(InputLine line) {
        return line.getWord(0).equals("create");
    }

    @Override
    public void process(InputLine line) {
        String query = "CREATE TABLE IF NOT EXISTS ";
        for (int i = 1; i < line.countWords(); i++) {
            query += line.getWord(i) + " ";
        }
        dbManager.createTable(query);
        String tableName = line.getWord(1);
        view.write(String.format("Таблица %s создана", tableName));
    }
}
