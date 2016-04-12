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

    public void showTable(String tableName, ArrayList<DataSet> tableData) {
        int fieldLength = 15; //TODO посчитать длины в каждой колонке
        String format = "%15s|";

        printHeaderOfTable(tableName, fieldLength);
        for (int i = 0; i < tableData.size(); i++) {
            view.write("|" + tableData.get(i).getValuesFormated(format) + "|");
        }
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

    private String repeatString(String s, int times) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < times; i++) {
            result.append(s);
        }
        return result.toString();
    }
}
