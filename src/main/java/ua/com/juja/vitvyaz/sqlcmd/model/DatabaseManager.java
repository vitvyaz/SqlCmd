package ua.com.juja.vitvyaz.sqlcmd.model;

import java.util.ArrayList;

/**
 * Created by Vitalii Viazovoi on 28.03.2016.
 */
public interface DatabaseManager {
    void connect(String dataBase, String user, String password);

    ArrayList<String> getTableNames();

    void insertRow(String tableName, DataSet dataSet);

    void updateQuery(String tableName, DataSet dataToChange, DataSet condition);

    ArrayList<String> getTableColumns(String tableName);

    ArrayList<DataSet> getTableData(String tableName);

    ArrayList<DataSet> getTableData(String tableName, int limit, int offset);

    DataSet getRow(String tableName, String rowId);

    boolean isTableExist(String tableName);

    void clearTable(String tableName);

    void dropTable(String tableName);

    void createTable(String query);

}
