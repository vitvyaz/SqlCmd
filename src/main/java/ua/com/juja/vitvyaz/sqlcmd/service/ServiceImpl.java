package ua.com.juja.vitvyaz.sqlcmd.service;

import ua.com.juja.vitvyaz.sqlcmd.model.DataSet;
import ua.com.juja.vitvyaz.sqlcmd.model.DatabaseManager;
import ua.com.juja.vitvyaz.sqlcmd.model.JDBCPosgreManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Виталий on 27.08.2016.
 */
public class ServiceImpl implements Service {

    @Override
    public List<String> commandsList() {
        return Arrays.asList("help", "connect", "menu", "find");
    }

    @Override
    public DatabaseManager connect(String databaseName, String userName, String password) {
        DatabaseManager dbManager = new JDBCPosgreManager();
        dbManager.connect(databaseName, userName, password);
        return dbManager;
    }

    @Override
    public List<List<String>> find(DatabaseManager dbManager, String tableName) {
        List<List<String>> result = new LinkedList();

        List<String> columns = new ArrayList<>(dbManager.getTableColumns(tableName));
        result.add(columns);

        List<DataSet> tableData = dbManager.getTableData(tableName);
        for (DataSet dataSet : tableData) {
            List<String> row = new ArrayList<>(columns.size());
            for (String column : columns) {
                row.add(dataSet.getValue(column).toString());
            }
            result.add(row);
        }
        return result;
    }
}
