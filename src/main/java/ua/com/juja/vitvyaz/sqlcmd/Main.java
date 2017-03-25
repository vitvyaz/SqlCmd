package ua.com.juja.vitvyaz.sqlcmd;

import ua.com.juja.vitvyaz.sqlcmd.controller.MainController;
import ua.com.juja.vitvyaz.sqlcmd.model.DatabaseManager;
import ua.com.juja.vitvyaz.sqlcmd.model.JDBCPostgreManager;
import ua.com.juja.vitvyaz.sqlcmd.view.Console;
import ua.com.juja.vitvyaz.sqlcmd.view.View;

/**
 * Created by Vitalii Viazovoi on 09.03.2016.
 */
public class Main {
    public static void main(String[] args) {

        DatabaseManager dbManager = new JDBCPostgreManager();
        View view = new Console();

        MainController controller = new MainController(dbManager, view);
        controller.run();
    }
}
