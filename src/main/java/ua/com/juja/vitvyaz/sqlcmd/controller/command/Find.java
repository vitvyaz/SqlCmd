package ua.com.juja.vitvyaz.sqlcmd.controller.command;

import ua.com.juja.vitvyaz.sqlcmd.controller.command.util.InputLine;
import ua.com.juja.vitvyaz.sqlcmd.model.DataSet;
import ua.com.juja.vitvyaz.sqlcmd.model.DatabaseManager;
import ua.com.juja.vitvyaz.sqlcmd.view.View;

import java.util.ArrayList;

/**
 * Created by Vitalii Viazovoi on 12.04.2016.
 */
public class Find extends Command {

    public Find(View view, DatabaseManager dbManager) {
        super(view, dbManager);
        description = "\tfind tableName [LIMIT OFFET]\n" +
                "\t\tвывести содержимое таблицы [LIMIT - количество строк OFFSET - начальная строка]";
        formats = new String[] {"find tableName", "find tableName LIMIT OFFET"};
    }

    @Override
    public boolean canProcess(InputLine line) {
        return line.getWord(0).equals("find");
    }

    @Override
    public void process(InputLine line) {
        if (formats != null) {
            line.parametersNumberValidation(formats);
        }

        String tableName = line.getWord(1);
        line.tableNameValidation(dbManager, tableName);

        ArrayList<DataSet> tableData = new ArrayList<>();
        if (line.countWords() == 2) {
            tableData = dbManager.getTableData(tableName);
        } else if (line.countWords() == 4) {
            int limit = Integer.valueOf(line.getWord(2));
            int offset = Integer.valueOf(line.getWord(3));
            tableData = dbManager.getTableData(tableName, limit, offset);
        }
        showTable(tableName, tableData);
    }

    public void showTable(String tableName, ArrayList<DataSet> tableData) {
        int[] columnsLengths = getColumnsLengths(tableName, tableData);

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

        StringBuilder columnsNames = new StringBuilder("|");
        for (int i = 0; i < tableColumns.size(); i++) {
            String format = " %" + columnsLengths[i] + "s |";
            columnsNames.append(String.format(format, tableColumns.get(i)));
        }

        view.write(repeatString("-", columnsNames.length()));
        view.write(columnsNames.toString());
        view.write(repeatString("-", columnsNames.length()));
    }

    private String repeatString(String s, int times) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < times; i++) {
            result.append(s);
        }
        return result.toString();
    }
}
