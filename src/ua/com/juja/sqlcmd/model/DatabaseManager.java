package ua.com.juja.sqlcmd.model;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Vitalii Viazovoi on 22.02.2016.
 */
public class DatabaseManager {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void connect(String dataBase, String user, String password) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("You should add JDBC lib: org.postgresql");
        }
        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/" + dataBase, user, password);
        } catch (SQLException e) {
            System.out.println("Не удается подключиться к базе данных: " + dataBase + " имя пользователя: " + user);
            System.out.println(("Проверьте правильность имени базы данных, пользователя и пароля"));
            connection = null;
        }
    }

    public ArrayList<String> getTableNames() {
        ArrayList<String> result = new ArrayList<>();
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema='public'";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                result.add(rs.getString(1));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Ошибка sql query: " + sql);
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<DataSet> getQueryData(String sql) {
        ArrayList<DataSet> tableData = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                DataSet dataSet = new DataSet();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    dataSet.add(rsmd.getColumnName(i), rs.getObject(i));
                }
                tableData.add(dataSet);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Ошибка sql query: " + sql);
            e.printStackTrace();
        }
        return tableData;
    }

    public boolean isTableExist(String tableName) {
        ArrayList<String> tableNames = getTableNames();
        for (String item : tableNames) {
            if (item.equals(tableName)) {
                return true;
            }
        }
        return false;
    }

    public void execQuery(String sql) {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Ошибка sql query: " + sql);
            e.printStackTrace();
        }
    }

    public void insertRow(String tableName, DataSet dataSet) {
        String sql = "INSERT INTO " + tableName + " (" + dataSet.getNamesFormated("%s ,") + ") "
                + "VALUES (" + dataSet.getValuesFormated("'%s' ,") + ")";
        execQuery(sql);
    }

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
        System.out.println("Запрос на апдейт:" + sql);
        execQuery(sql);
    }
}

