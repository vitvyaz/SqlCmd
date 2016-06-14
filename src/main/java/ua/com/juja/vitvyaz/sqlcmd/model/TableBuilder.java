package ua.com.juja.vitvyaz.sqlcmd.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Vitalii Viazovoi on 13.06.2016.
 */
public class TableBuilder {
    private Set<String> columns;
    private List<DataSet> data;

    public TableBuilder(Set<String> tableColumns, List<DataSet> tableData) {
        columns = tableColumns;
        data = tableData;
    }

    public TableBuilder(DataSet tableData) {
        columns = tableData.getNames();
        data = new LinkedList<DataSet>();
        data.add(tableData);
    }

    @Override
    public String toString() {
        int[] columnsLengths = getColumnsLengths(columns);

        StringBuilder columnsNames = new StringBuilder("|");
        int i = 0;
        for (String column : columns) {
            String format = " %" + columnsLengths[i++] + "s |";
            columnsNames.append(String.format(format, column));
        }
        String line = repeatString("-", columnsNames.length());

        StringBuilder result = new StringBuilder(line + "\n");
        result.append(columnsNames + "\n");
        result.append(line);

        if (data.size() > 0) {
            result.append("\n");
            for (i = 0; i < data.size(); i++) {
                List<Object> values = data.get(i).getValues();
                result.append("|");
                int columnIndex = 0;
                for (Object value : values) {
                    String format = " %" + columnsLengths[columnIndex++] + "s |";
                    result.append(String.format(format, value));
                }
                result.append("\n");
            }
            result.append(line);
        }
        return result.toString();
    }

    private int[] getColumnsLengths(Set<String> columns) {
        int[] result = new int[columns.size()];
        int i = 0;
        for (String column : columns) {
            result[i++] = column.length();
        }

        for (DataSet row : data) {
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

    private String repeatString(String s, int times) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < times; i++) {
            result.append(s);
        }
        return result.toString();
    }
}
