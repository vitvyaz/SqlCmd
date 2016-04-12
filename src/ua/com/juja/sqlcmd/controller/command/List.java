package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.JDBCPosgreManager;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Vitalii Viazovoi on 12.04.2016.
 */
public class List implements Command {

    private View view;
    private DatabaseManager dbManager;

    public List(View view, DatabaseManager dbManager) {
        this.view = view;
        this.dbManager = dbManager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("list");
    }

    @Override
    public void process(String[] arrayCommand) {
        view.write(dbManager.getTableNames().toString());
    }
}
