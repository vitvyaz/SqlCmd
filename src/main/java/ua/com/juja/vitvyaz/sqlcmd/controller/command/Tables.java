package ua.com.juja.vitvyaz.sqlcmd.controller.command;

import ua.com.juja.vitvyaz.sqlcmd.controller.command.util.InputLine;
import ua.com.juja.vitvyaz.sqlcmd.model.DatabaseManager;
import ua.com.juja.vitvyaz.sqlcmd.model.JDBCPosgreManager;
import ua.com.juja.vitvyaz.sqlcmd.view.View;

/**
 * Created by Vitalii Viazovoi on 12.04.2016.
 */
public class Tables extends Command {

    public Tables(View view, DatabaseManager dbManager) {
        super(view, dbManager);
        description = "\tlist\n" +
                "\t\tвывести список таблиц";
        formats = new String[] {"tables"};
    }

    @Override
    public boolean canProcess(InputLine line) {
        return line.getWord(0).equals("tables");
    }

    @Override
    public void process(InputLine line) {
        view.write(dbManager.getTableNames().toString());
    }
}
