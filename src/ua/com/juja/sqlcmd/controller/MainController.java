package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.controller.command.*;
import ua.com.juja.sqlcmd.controller.command.util.InputLine;
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
        Help commandHelp = new Help(view);
        commands = new Command[]{
                commandHelp,
                new Exit(view),
                new List(view, dbManager),
                new Find(view, dbManager),
                new Update(view, dbManager),
                new Insert(view, dbManager),
                new Clear(view, dbManager),
                new Create(view, dbManager),
                new Drop(view, dbManager),
                new Unsupported(view)
        };
        commandHelp.setCommands(commands);
    }

    public void run() {
        if (!connectDB()) {
            return;
        }

        while (true) {
            view.write("Введите команду:");
            InputLine input = new InputLine(view.read());
            for (Command command : commands) {
                if (command.canProcess(input)) {
                    try {
                        command.process(input);
                    } catch (Exception e) {
                        printError(e);
                    }
                    break;
                }
            }
            if (input.getWord(0).equals("exit")) {
                return;
            }
        }
    }

    public boolean connectDB() {
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
                    return false;
                }
            }
        }
        view.write("Подключение к базе данных выполнено");
        return true;
    }

    private void printError(Exception e) {
        String message = e.getMessage();
        Throwable cause = e.getCause();
        if (cause != null) {
            message += " " + cause.getMessage();
        }
        view.write(message);
    }


}
