package ua.com.juja.vitvyaz.sqlcmd.controller.command;

import ua.com.juja.vitvyaz.sqlcmd.controller.command.util.InputLine;
import ua.com.juja.vitvyaz.sqlcmd.model.DataSet;
import ua.com.juja.vitvyaz.sqlcmd.model.DatabaseManager;
import ua.com.juja.vitvyaz.sqlcmd.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

        List<DataSet> tableData = new ArrayList<>();
        if (line.countWords() == 2) {
            tableData = dbManager.getTableData(tableName);
        } else if (line.countWords() == 4) {
            int limit = Integer.valueOf(line.getWord(2));
            int offset = Integer.valueOf(line.getWord(3));
            tableData = dbManager.getTableData(tableName, limit, offset);
        }
        showTable(tableName, tableData);
    }

    public void showTable(String tableName, List<DataSet> tableData) {
        int[] columnsLengths = getColumnsLengths(tableName, tableData);

        printHeaderOfTable(tableName, columnsLengths);
        for (int i = 0; i < tableData.size(); i++) {
            List<Object> values = tableData.get(i).getValues();
            String s = "|";
            int columnIndex = 0;
            for (Object value : values) {
                String format = " %" + columnsLengths[columnIndex++] + "s |";
                s += String.format(format, value);
            }
            view.write(s);
        }
    }

    private int[] getColumnsLengths(String tableName, List<DataSet> tableData) {
        Set<String> tableColumns = dbManager.getTableColumns(tableName);
        int[] result = new int[tableColumns.size()];
        int i = 0;
        for (String column : tableColumns) {
            result[i++] = column.length();
        }

        for (DataSet row : tableData) {
            List<Object> values = row.getValues();
            int index = 0;
            for (Object value : values) {
                if (value != null) {
                    int valueLength = value.toString().length();
                    if (valueLength > result[index]) {
                        result[index] = valueLength;
                    }
                }
                index++;
            }
        }
        return result;
    }

    private void printHeaderOfTable(String tableName, int[] columnsLengths) {
        Set<String> tableColumns = dbManager.getTableColumns(tableName);

        StringBuilder columnsNames = new StringBuilder("|");
        int i = 0;
        for (String column : tableColumns) {
            String format = " %" + columnsLengths[i++] + "s |";
            columnsNames.append(String.format(format, column));
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
