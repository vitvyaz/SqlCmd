package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.ArrayList;

/**
 * Created by Vitalii Viazovoi on 22.02.2016.
 */
public class MainController {
    private View view;
    private DatabaseManager dbManager;

    public MainController(DatabaseManager dbManager, View view) {
        this.view = view;
        this.dbManager = dbManager;
    }

    public void run() {
        connectDB();
        while (true) {
            view.write("Введите команду:");
            String command = view.read();
            command = command.replaceAll("\\s+"," ");
            String[] arrayCommand = command.split(" ");
            switch (arrayCommand[0]) {
                case "exit":
                    view.write("До свидания!");
                    return;
                case "list":
                    view.write(dbManager.getTableNames().toString());
                    break;
                case "find":
                    showTable(arrayCommand);
                    break;
                default:
                    view.write("Введите правильно команду. (help - вывод списка команд)");
                    break;
            }
        }
    }

    private void showTable(String[] arrrayCommand) {
        String tableName = arrrayCommand[1];
        if (!dbManager.isTableExist(tableName)){
            view.write("Нет такой таблицы. Доступны таблицы:");
            view.write(dbManager.getTableNames().toString());
            return;
        }

        if (arrrayCommand.length == 2) {
            String sql = "SELECT * FROM " + tableName;
            showQuery(tableName, sql);
        } else if (arrrayCommand.length == 4) {
            int limit = Integer.valueOf(arrrayCommand[2]);
            int offset = Integer.valueOf(arrrayCommand[3]);
            String sql = "SELECT * FROM " + tableName + " ORDER BY id LIMIT " + limit + " OFFSET "+ offset;
            showQuery(tableName, sql);
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
            dbManager.connect(dbName, userName, userPass);
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

    public void showQuery(String tableName, String sql) {
        ArrayList<DataSet> tableData = dbManager.getQueryData(sql);
        if (tableData.size() == 0) {
            view.write("Нет строк для вывода!");
            return;
        }
        DataSet dataSet = tableData.get(0);
        String format = "%15s|";
        view.write(dataSet.getNamesFormated(format));
        for (int i = 0; i < tableData.size(); i++) {
            view.write(tableData.get(i).getValuesFormated(format));
        }
    }

}
