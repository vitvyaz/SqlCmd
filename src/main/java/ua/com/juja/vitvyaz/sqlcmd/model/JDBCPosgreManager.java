package ua.com.juja.vitvyaz.sqlcmd.model;

import ua.com.juja.vitvyaz.sqlcmd.controller.command.util.Configuration;

import java.sql.*;
import java.util.*;

/**
 * Created by Vitalii Viazovoi on 22.02.2016.
 */
public class JDBCPosgreManager implements DatabaseManager {

    private Connection connection;

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public void connect(String dataBase, String user, String password) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Не подключена JDBC lib: org.postgresql", e);
        }
        try {
            Configuration configuration = new Configuration();
            String databaseUrl = String.format("%s%s:%s/%s",
                    configuration.getDriver(),
                    configuration.getServerName(),
                    configuration.getPortNumber(),
                    dataBase);
            connection = DriverManager.getConnection(databaseUrl, user, password);
        } catch (SQLException e) {
            connection = null;
            throw new RuntimeException("Не удается подключиться к базе данных: " + dataBase + " имя пользователя: " + user, e);
        }
    }

    @Override
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Set<String> getTableNames() {
        Set<String> result = new HashSet<>();
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema='public'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql))
        {
            while (rs.next()) {
                result.add(rs.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка sql query: " + sql, e);
        }
        return result;
    }

    private List<DataSet> getQueryData(String sql) {
        List<DataSet> tableData = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql))
        {
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                DataSet dataSet = new DataSet();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    dataSet.add(rsmd.getColumnName(i), rs.getObject(i));
                }
                tableData.add(dataSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка sql query: " + sql, e);
        }
        return tableData;
    }

    @Override
    public boolean existTable(String tableName) {
        Set<String> tableNames = getTableNames();
        for (String item : tableNames) {
            if (item.equals(tableName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void clearTable(String tableName) {
        execQuery("DELETE FROM " + tableName);
    }

    @Override
    public void dropTable(String tableName) {
        execQuery("DROP TABLE IF EXISTS " + tableName);
    }

    @Override
    public void createTable(String query) {
        query = "CREATE TABLE IF NOT EXISTS " + query;
        execQuery(query);
    }

    private void execQuery(String sql)  {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка sql query: " + sql, e);
        }
    }

    @Override
    public void insertRow(String tableName, DataSet dataSet) {
        String sql = "INSERT INTO " + tableName + " (" + dataSet.getNamesFormated("%s ,") + ") "
                + "VALUES (" + dataSet.getValuesFormated("'%s' ,") + ")";
        execQuery(sql);
    }

    @Override
    public void update(String tableName, DataSet dataToChange, DataSet condition) {
        String dataToUpdate = " SET (" + dataToChange.getNamesFormated(" %s,") + ") = "
                + "(" + dataToChange.getValuesFormated(" '%s',") + ")";

        String conditionToUpdate = getConditionString(condition);

        String sql = "UPDATE " + tableName + dataToUpdate + conditionToUpdate;
        execQuery(sql);
    }

    private String getConditionString(DataSet condition) {
        Set<String> conditionNames = condition.getNames();
        Iterator<String> iteratorNames = conditionNames.iterator();
        String conditionToUpdate = "";
        String name;
        if (condition.size() > 0) {
            name = iteratorNames.next();
            conditionToUpdate = " WHERE " + name + " = '" + condition.getValue(name) + "'";
        }
        for (int i = 1; i < condition.size(); i++) {
            name = iteratorNames.next();
            conditionToUpdate += " AND " + name + " = '" + condition.getValue(name) + "'";
        }
        return conditionToUpdate;
    }

    @Override
    public Set<String> getTableColumns(String tableName) {
        if (!existTable(tableName)) {
            throw new IllegalArgumentException("Ошибка! Нет таблицы с именем: " + tableName);
        }
        Set<String> result = new LinkedHashSet<>();
        String sql = "SELECT column_name FROM INFORMATION_SCHEMA.COLUMNS where table_name = '" + tableName + "'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql))
        {
            while (rs.next()) {
                result.add(rs.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка sql query: " + sql, e);
        }
        return result;
    }

    @Override
    public List<DataSet> getTableData(String tableName) {
        String sql = "SELECT * FROM " + tableName;
        return getQueryData(sql);
    }

    @Override
    public List<DataSet> getTableData(String tableName, int limit, int offset) {
        String sql = "SELECT * FROM " + tableName + " ORDER BY id LIMIT " + limit + " OFFSET " + offset;
        return getQueryData(sql);
    }

    @Override
    public DataSet getRow(String tableName, String rowId) {
        DataSet result = new DataSet();
        String sql = "SELECT * FROM " + tableName + " WHERE id=" + rowId;
        List<DataSet> queryData = getQueryData(sql);
        if (queryData.size() != 0) {
            result = queryData.get(0);
        }
        return result;
    }
}

