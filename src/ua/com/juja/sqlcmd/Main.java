package ua.com.juja.sqlcmd;

import java.sql.*;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Vitalii Viazovoi on 17.02.2016.
 */
public class Main {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Scanner in = new Scanner(System.in);
        String dbName = "";
        String user = "";




        DatabaseManager dbManager = new DatabaseManager();
        dbManager.connect("sqlcmd", "postgres", "postgres");
        System.out.println(dbManager.getTableNames().toString());
        dbManager.showTable("users");


    }

    public static void test() throws ClassNotFoundException, SQLException {


        //connect to db
        Class.forName("org.postgresql.Driver");

        Connection connection = null;

        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/sqlcmd", "postgres",
                "postgres");


        //insert
        Statement stmt = connection.createStatement();
        Random random = new Random();
        int i = random.nextInt();
        String userName = "user" + i;
        String userPass = "pass" + i;

        String sql = "INSERT INTO users (name, password) "
                + "VALUES ('" + userName + "', '" + userPass + "');";
        stmt.executeUpdate(sql);


        //delete
        sql = "DELETE FROM users WHERE id=5;";
        stmt.executeUpdate(sql);

        //update
        sql = "UPDATE users SET password = '*****' where ID=111;";
        stmt.executeUpdate(sql);

        // select
        sql = "SELECT * FROM users;";
        select(stmt, sql);




    }

    public static void select(Statement stmt, String sql) throws SQLException {
        ResultSet rs = stmt.executeQuery(sql);
        System.out.println("id      |name        |password");
        System.out.println("============================");
        while ( rs.next() ) {
            int id = rs.getInt("id");
            String  name = rs.getString("name");
            String  password = rs.getString("password");
            System.out.print( id + "|");
            System.out.print(  name + "|");
            System.out.println(  password );
        }
    }

}
