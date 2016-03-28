package ua.com.juja.sqlcmd.model;

import java.util.ArrayList;

/**
 * Created by Vitalii Viazovoi on 28.03.2016.
 */
public interface DatabaseManager {
    void connect(String dataBase, String user, String password);

    ArrayList<String> getTableNames();

    ArrayList<DataSet> getQueryData(String sql);

    void insertRow(String tableName, DataSet dataSet);

    void updateQuery(String tableName, DataSet dataToChange, DataSet condition);
}
