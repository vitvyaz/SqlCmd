package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.ArrayList;

/**
 * Created by Vitalii Viazovoi on 12.04.2016.
 */
public class Find implements Command {

    private View view;
    private DatabaseManager dbManager;

    public Find(View view, DatabaseManager dbManager) {
        this.view = view;
        this.dbManager = dbManager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("find");
    }

    @Override
    public void process(String[] arrayCommand) {
        if (!(arrayCommand.length == 2 || arrayCommand.length == 4)) {
            view.write("Не верное количество параметров команды find");
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

    public void showTable(String tableName, ArrayList<DataSet> tableData) {
        int[] columnsLengths = getColumnsLengths(tableName, tableData);
//        int fieldLength = 15; //TODO посчитать длины в каждой колонке
//        String format = "%15s|";

        printHeaderOfTable(tableName, columnsLengths);
        for (int i = 0; i < tableData.size(); i++) {
            DataSet row = tableData.get(i);
            String s = "|";
            for (int j = 0; j < row.size(); j++) {
                String format = " %" + columnsLengths[j] + "s |";
                s += String.format(format, row.getValue(j));
            }
            view.write(s);
        }
    }

    private int[] getColumnsLengths(String tableName, ArrayList<DataSet> tableData) {
        ArrayList<String> tableColumns = dbManager.getTableColumns(tableName);
        int[] result = new int[tableColumns.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = tableColumns.get(i).length();
        }

        for (DataSet row : tableData) {
            for (int i = 0; i < row.size(); i++) {

                if (row.getValue(i) != null) {
                    int valueLength = row.getValue(i).toString().length();
                    if (valueLength > result[i]) {
                        result[i] = valueLength;
                    }
                }
            }
        }
        return result;
    }

    private void printHeaderOfTable(String tableName, int[] columnsLengths) {
        ArrayList<String> tableColumns = dbManager.getTableColumns(tableName);

        int rowLength = 1;
        for (int colLength : columnsLengths) {
            rowLength += colLength + 3; // 3 - это 2 пробела и один разделитель | колонки
        }

        view.write(repeatString("-", rowLength));
        String s = "|";
        for (int i = 0; i < tableColumns.size(); i++) {
            String format = " %" + columnsLengths[i] + "s |";
            s += String.format(format, tableColumns.get(i));
        }
        view.write(s);
        view.write(repeatString("-", rowLength));
    }

    private String repeatString(String s, int times) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < times; i++) {
            result.append(s);
        }
        return result.toString();
    }
}
