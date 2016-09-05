package ua.com.juja.vitvyaz.sqlcmd.service;

import ua.com.juja.vitvyaz.sqlcmd.model.DatabaseManager;
import java.util.List;

/**
 * Created by Виталий on 27.08.2016.
 */
public interface Service {
    List<String> commandsList();

    DatabaseManager connect(String databaseName, String userName, String password);

    List<List<String>> find(DatabaseManager manager, String tableName);
}
