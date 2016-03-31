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
                    doFind(arrayCommand);
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

    private void doHelp() {
        view.write("Существующие команды:");
        view.write("\thelp");
        view.write("\t\tвывести список команд");
        view.write("\tlist");
        view.write("\t\tвывести список таблиц");
        view.write("\tfind tableName [LIMIT OFFET] ");
        view.write("\t\tвывести содержимое таблицы [LIMIT - количество строк OFFSET - начальная строка]");
        view.write("\texit");
        view.write("\t\tвыход из программы");
    }

    private void doFind(String[] arrrayCommand) {
        if (arrrayCommand.length < 2) {
            view.write("Не введено имя таблицы");
            return;
        }

        String tableName = arrrayCommand[1];
        if (!dbManager.isTableExist(tableName)){
            view.write("Нет такой таблицы. Доступны таблицы:");
            view.write(dbManager.getTableNames().toString());
            return;
        }

        if (arrrayCommand.length == 2) {
            String sql = "SELECT * FROM " + tableName;
            showTable(tableName, sql);
        } else if (arrrayCommand.length == 4) {
            int limit = Integer.valueOf(arrrayCommand[2]);
            int offset = Integer.valueOf(arrrayCommand[3]);
            String sql = "SELECT * FROM " + tableName + " ORDER BY id LIMIT " + limit + " OFFSET "+ offset;
            showTable(tableName, sql);
        }
    }

    private String repeatString(String s, int times) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < times; i++) {
            stringBuffer.append(s);
        }
        return stringBuffer.toString();
    }

    private void printHeaderOfTable(String tableName, int fieldLength) {
        ArrayList<String> tableColumns = dbManager.getTableColumn(tableName);
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

    public void showTable(String tableName, String sql) {
        ArrayList<DataSet> tableData = dbManager.getQueryData(sql);
        int fieldLength = 15;
        String format = "%15s|";

        printHeaderOfTable(tableName, fieldLength);
        for (int i = 0; i < tableData.size(); i++) {
            view.write("|"+tableData.get(i).getValuesFormated(format)+"|");
        }
    }

}
