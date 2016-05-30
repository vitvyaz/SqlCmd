package ua.com.juja.vitvyaz.sqlcmd.model;

import java.util.List;

/**
 * Created by Vitalii Viazovoi on 28.03.2016.
 */
public interface DatabaseManager {
    void connect(String dataBase, String user, String password);

    void disConnect();

    List<String> getTableNames();

    void insertRow(String tableName, DataSet dataSet);

    void updateQuery(String tableName, DataSet dataToChange, DataSet condition);

    List<String> getTableColumns(String tableName);

    List<DataSet> getTableData(String tableName);

    List<DataSet> getTableData(String tableName, int limit, int offset);

    DataSet getRow(String tableName, String rowId);

    boolean isConnected();

    boolean isTableExist(String tableName);

    void clearTable(String tableName);

    void dropTable(String tableName);

    void createTable(String query);

}
