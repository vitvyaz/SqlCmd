package ua.com.juja.vitvyaz.sqlcmd.model;

import java.util.List;
import java.util.Set;

/**
 * Created by Vitalii Viazovoi on 28.03.2016.
 */
public interface DatabaseManager {

    void connect(String dataBase, String user, String password);

    void disconnect();

    Set<String> getTableNames();

    Set<String> getTableColumns(String tableName);

    List<DataSet> getTableData(String tableName);

    List<DataSet> getTableData(String tableName, int limit, int offset);

    DataSet getRow(String tableName, String rowId);

    void insertRow(String tableName, DataSet dataSet);

    void update(String tableName, DataSet dataToChange, DataSet condition);

    void clearTable(String tableName);

    void dropTable(String tableName);

    void createTable(String query);

    boolean isConnected();

    boolean existTable(String tableName);

}
