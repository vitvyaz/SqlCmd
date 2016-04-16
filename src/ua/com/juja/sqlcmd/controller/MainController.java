package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.controller.command.*;
import ua.com.juja.sqlcmd.model.JDBCPosgreManager;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Vitalii Viazovoi on 22.02.2016.
 */
public class MainController {
    private Command[] commands;
    private View view;
    private JDBCPosgreManager dbManager;

    public MainController(JDBCPosgreManager dbManager, View view) {
        this.view = view;
        this.dbManager = dbManager;
        commands = new Command[] {
                new Exit(view),
                new List(view, dbManager),
                new Find(view, dbManager),
                new Update(view, dbManager),
                new Help(view),
                new Unsupported(view)
        };
    }

    public void run() {
        try {
            connectDB();
            doCommand();
        } catch (ExitException e) {
            //do nothing
            return;
        }
    }

    private void doCommand() {
        while (true) {
            view.write("Введите команду:");
            String input = view.read();
            input = input.replaceAll("\\s+", " ");
            String[] arrayCommand = input.split(" ");
            for (Command command : commands) {
                if (command.canProcess(arrayCommand[0])) {
                    command.process(arrayCommand);
                    break;
                }
            }
        }
    }

    public void connectDB() {
        while (dbManager.getConnection() == null) {
            view.write("Введите название базы данных(sqlcmd): ");
            String dbName = view.read();
            view.write("Введите имя пользователя: ");
            String userName = view.read();
            view.write("Введите пароль: ");
            String userPass = view.read();
            try {
                dbManager.connect(dbName, userName, userPass);
            } catch (Exception e) {
                printError(e);
            }
            if (dbManager.getConnection() == null) {
                view.write("Повторить попытку? (yes/no):");
                String input = view.read();
                if (!input.equals("yes")) {
                    view.write("До свидания!");
                    System.exit(0);
                }
            }
        }
        view.write("Подключение к базе данных выполнено");
    }

    private void printError(Exception e) {
        String message = e.getMessage();
        Throwable cause = e.getCause();
        if (cause != null) {
            message += " " + cause.getMessage();
        }
        view.write("Ошибка подключения к БД: " + message);
    }


}
