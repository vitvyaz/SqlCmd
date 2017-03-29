package ua.com.juja.vitvyaz.sqlcmd.service;

import org.springframework.stereotype.Component;
import ua.com.juja.vitvyaz.sqlcmd.model.DataSet;
import ua.com.juja.vitvyaz.sqlcmd.model.DatabaseManager;

import java.util.*;

/**
 * Created by Виталий on 27.08.2016.
 */
@Component
public abstract class ServiceImpl implements Service {

    @Override
    public List<String> commandsList() {
        return Arrays.asList("help", "connect", "tables", "create");
    }

    @Override
    public DatabaseManager connect(String databaseName, String userName, String password) throws ServiceException {
        try {
            DatabaseManager dbManager = getManager();
            dbManager.connect(databaseName, userName, password);
            return dbManager;
        } catch (Exception e) {
            throw new ServiceException("Connection error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<List<String>> find(DatabaseManager dbManager, String tableName) throws ServiceException {
        List<List<String>> result = new LinkedList();
        try {
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
        } catch (Exception e) {
            throw new ServiceException("Command find error: " + e.getMessage(), e);
        }
    }

    @Override
    public Set<String> tables(DatabaseManager dbManager) throws ServiceException {
        try {
            return dbManager.getTableNames();
        } catch (Exception e) {
            throw new ServiceException("Command tables error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> columns(DatabaseManager dbManager, String tableName) throws ServiceException {
        try {
            return new ArrayList<>(dbManager.getTableColumns(tableName));
        } catch (Exception e) {
            throw new ServiceException("Command columns error: " + e.getMessage(), e);
        }
    }

    protected abstract DatabaseManager getManager();
}
