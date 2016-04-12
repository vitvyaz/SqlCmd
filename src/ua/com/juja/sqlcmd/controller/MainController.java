package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.JDBCPosgreManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.ArrayList;

/**
 * Created by Vitalii Viazovoi on 22.02.2016.
 */
public class MainController {
    private View view;
    private JDBCPosgreManager dbManager;

    public MainController(JDBCPosgreManager dbManager, View view) {
        this.view = view;
        this.dbManager = dbManager;
    }

    public void run() {
        connectDB();
        while (true) {
            view.write("Введите команду:");
            String command = view.read();
            command = command.replaceAll("\\s+", " ");
            String[] arrayCommand = command.split(" ");
            switch (arrayCommand[0]) {
                case "exit":
                    view.write("До свидания!");
                    return;
                case "list":
                    view.write(dbManager.getTableNames().toString());
                    break;
                case "find":
                    doFind(arrayCommand);
                    break;
                case "update":
                    doUpdate(arrayCommand);
                    break;
                case "help":
                    doHelp();
                    break;
                default:
                    view.write("Введите правильно команду. (help - вывод списка команд)");
                    break;
            }
        }
    }

    private void doUpdate(String[] arrayCommand) {
        if (arrayCommand.length != 3) {
            view.write("Неправильное количество параметров для команды update. Должно быть 3");
            return;
        }

        String tableName = arrayCommand[1];
        if (!isTableExist(tableName)) {
            view.write("Нет такой таблицы");
            return;
        }

        String rowId = arrayCommand[2];
        DataSet rowData = dbManager.getRow(tableName, rowId);
        if (rowData.size() == 0) {
            view.write("В таблице " + tableName + "нет строки с id: " + rowId);
            return;
        }

        view.write(rowData.getTable());
        view.write("Введите данные к изменению в формате: field1 newValue1 field2 newValue2 ... ");
        String fieldsValues = view.read();
        fieldsValues = fieldsValues.replaceAll("\\s+", " ");
        String[] arrayFieldValues = fieldsValues.split(" ");

        if (arrayFieldValues.length % 2 == 1) {
            view.write("Ошибка! Нечетное количество параметров");
            return;
        }

        DataSet dataToChange = new DataSet();
        for (int i = 0; i < arrayFieldValues.length; i += 2) {
            dataToChange.add(arrayFieldValues[i], arrayFieldValues[i + 1]);
        }
        DataSet condition = new DataSet();
        condition.add("id", rowId);
        dbManager.updateQuery(tableName, dataToChange, condition);
        view.write("Измененая строка");
        view.write(rowData.getTable());
    }

    private void doHelp() {
        view.write("Существующие команды:");
        view.write("\thelp");
        view.write("\t\tвывести список команд");
        view.write("\tlist");
        view.write("\t\tвывести список таблиц");
        view.write("\tfind tableName [LIMIT OFFET] ");
        view.write("\t\tвывести содержимое таблицы [LIMIT - количество строк OFFSET - начальная строка]");
        view.write("\tupdate tableName ID");
        view.write("\t\tизменить строку таблицы tableName (ID - идентификатор строки)");
        view.write("\texit");
        view.write("\t\tвыход из программы");
    }

    private void doFind(String[] arrayCommand) {
        if (arrayCommand.length < 2) {
            view.write("Не введено имя таблицы");
            return;
        }

        String tableName = arrayCommand[1];
        if (!isTableExist(tableName)) {
            return;
        }
        ArrayList<DataSet> tableData;
        if (arrayCommand.length == 2) {
            tableData = dbManager.getTableData(tableName);
            showTable(tableName, tableData);
        } else if (arrayCommand.length == 4) {
            int limit = Integer.valueOf(arrayCommand[2]);
            int offset = Integer.valueOf(arrayCommand[3]);
            tableData = dbManager.getTableData(tableName, limit, offset);
            showTable(tableName, tableData);
        }
    }

    private boolean isTableExist(String tableName) {
        if (!dbManager.isTableExist(tableName)) {
            view.write("Нет такой таблицы. Доступны таблицы:");
            view.write(dbManager.getTableNames().toString());
            return false;
        }
        return true;
    }

    private String repeatString(String s, int times) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < times; i++) {
            result.append(s);
        }
        return result.toString();
    }

    private void printHeaderOfTable(String tableName, int fieldLength) {
        ArrayList<String> tableColumns = dbManager.getTableColumns(tableName);
        int rowLength = (fieldLength + 1) * tableColumns.size() + 1;

        view.write(repeatString("-", rowLength));
        String format = "%" + fieldLength + "s|";
        String s = "|";
        for (int i = 0; i < tableColumns.size(); i++) {
            s += String.format(format, tableColumns.get(i));
        }
        view.write(s);
        view.write(repeatString("-", rowLength));
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

    public void showTable(String tableName, ArrayList<DataSet> tableData) {
        int fieldLength = 15; //TODO посчитать длины в каждой колонке
        String format = "%15s|";

        printHeaderOfTable(tableName, fieldLength);
        for (int i = 0; i < tableData.size(); i++) {
            view.write("|" + tableData.get(i).getValuesFormated(format) + "|");
        }
    }

}
