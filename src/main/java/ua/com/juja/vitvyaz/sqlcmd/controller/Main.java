package ua.com.juja.vitvyaz.sqlcmd.controller;

import ua.com.juja.vitvyaz.sqlcmd.model.JDBCPosgreManager;
import ua.com.juja.vitvyaz.sqlcmd.view.Console;
import ua.com.juja.vitvyaz.sqlcmd.view.View;

/**
 * Created by Vitalii Viazovoi on 09.03.2016.
 */
public class Main {
    public static void main(String[] args) {
        JDBCPosgreManager dbManager = new JDBCPosgreManager();
        View view = new Console();

        MainController controller = new MainController(dbManager, view);
        controller.run();
    }
}
