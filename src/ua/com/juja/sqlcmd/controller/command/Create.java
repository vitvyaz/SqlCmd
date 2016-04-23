package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Vitalii Viazovoi on 23.04.2016.
 */
public class Create implements Command {
    private View view;
    private DatabaseManager dbManager;

    public Create(View view, DatabaseManager dbManager) {
        this.view = view;
        this.dbManager = dbManager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("create");
    }

    @Override
    public void process(String[] arrayCommand) {
        String query = "CREATE TABLE IF NOT EXISTS ";
        for (int i = 1; i < arrayCommand.length; i++) {
            query += arrayCommand[i] + " ";
        }
        dbManager.createTable(query);
        String tableName = arrayCommand[1];
        view.write(String.format("Таблица %s создана", tableName));
    }
}
