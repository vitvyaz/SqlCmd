package ua.com.juja.vitvyaz.sqlcmd.service;

import ua.com.juja.vitvyaz.sqlcmd.model.DatabaseManager;
import ua.com.juja.vitvyaz.sqlcmd.model.JDBCPosgreManager;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Виталий on 27.08.2016.
 */
public class ServiceImpl implements Service {
    DatabaseManager dbManager;

    public ServiceImpl() {
        dbManager = new JDBCPosgreManager();
    }

    @Override
    public List<String> commandsList() {
        return Arrays.asList("help", "connect", "menu");
    }

    @Override
    public void connect(String databaseName, String userName, String password) {
        dbManager.connect(databaseName, userName, password);
    }
}
