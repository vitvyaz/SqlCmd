package ua.com.juja.sqlcmd.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Vitalii Viazovoi on 22.02.2016.
 */
public class JDBCPosgreManager implements DatabaseManager {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void connect(String dataBase, String user, String password) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("You should add JDBC lib: org.postgresql", e);
        }
        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/" + dataBase, user, password);
        } catch (SQLException e) {
            connection = null;
            throw new RuntimeException("Не удается подключиться к базе данных: " + dataBase + " имя пользователя: " + user, e);
        }
    }

    @Override
    public ArrayList<String> getTableNames() {
        ArrayList<String> result = new ArrayList<>();
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema='public'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql))
        {
            while (rs.next()) {
                result.add(rs.getString(1));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка sql query: " + sql);
            e.printStackTrace();
        }
        Collections.sort(result);
        return result;
    }

    private ArrayList<DataSet> getQueryData(String sql) {
        ArrayList<DataSet> tableData = new ArrayList<>();
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
            System.out.println("Ошибка sql query: " + sql);
            e.printStackTrace();
        }
        return tableData;
    }

    @Override
    public boolean isTableExist(String tableName) {
        ArrayList<String> tableNames = getTableNames();
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
        execQuery(query);
    }

    public void execQuery(String sql)  {
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
    public void updateQuery(String tableName, DataSet dataToChange, DataSet condition) {
        String dataToUpdate = " SET (" + dataToChange.getNamesFormated(" %s,") + ") = "
                + "(" + dataToChange.getValuesFormated(" '%s',") + ")";

        String conditionToUpdate = "";
        if (condition.size() > 0) {
            conditionToUpdate = " WHERE " + condition.getName(0) + " = '" + condition.getValue(0) + "'";
        }
        for (int i = 1; i < condition.size(); i++) {
            conditionToUpdate += " AND " + condition.getName(i) + " = '" + condition.getValue(i) + "'";
        }

        String sql = "UPDATE " + tableName + dataToUpdate + conditionToUpdate;
        execQuery(sql);
    }

    @Override
    public ArrayList<String> getTableColumns(String tableName) {
        ArrayList<String> result = new ArrayList<>();
        String sql = "SELECT column_name FROM INFORMATION_SCHEMA.COLUMNS where table_name = '" + tableName + "'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql))
        {
            while (rs.next()) {
                result.add(rs.getString(1));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка sql query: " + sql);
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<DataSet> getTableData(String tableName) {
        String sql = "SELECT * FROM " + tableName;
        return getQueryData(sql);
    }

    @Override
    public ArrayList<DataSet> getTableData(String tableName, int limit, int offset) {
        String sql = "SELECT * FROM " + tableName + " ORDER BY id LIMIT " + limit + " OFFSET " + offset;
        return getQueryData(sql);
    }

    @Override
    public DataSet getRow(String tableName, String rowId) {
        DataSet result = new DataSet();
        String sql = "SELECT * FROM " + tableName + " WHERE id=" + rowId;
        ArrayList<DataSet> queryData = getQueryData(sql);
        if (queryData.size() != 0) {
            result = queryData.get(0);
        }
        return result;
    }
}

