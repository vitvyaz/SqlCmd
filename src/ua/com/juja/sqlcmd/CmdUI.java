package ua.com.juja.sqlcmd;

import java.io.Console;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Vitalii Viazovoi on 22.02.2016.
 */
public class CmdUI {
    String dbName;
    String userName;
    String userPass;
    Scanner in;
    DatabaseManager dbManager = new DatabaseManager();

    public CmdUI(String dbName, String userName, String userPass) {
        this.dbName = dbName;
        this.userName = userName;
        this.userPass = userPass;
    }

    public static void main(String[] args) {
        CmdUI cmdUI = new CmdUI();
        cmdUI.connectDB();
        while (true) {
//            if (cmdUI.in.hasNextLine()) {cmdUI.in.nextLine();}
            System.out.println("Введите команду:");
            String command = cmdUI.in.nextLine();
            command = command.replaceAll("\\s+"," ");
            String[] arrrayCommand = command.split(" ");
            switch (arrrayCommand[0]) {
                case "exit":
                    System.out.println("До свидания!");
                    return;
                case "list":
                    System.out.println(cmdUI.dbManager.getTableNames());
                    break;
                case "find":
                    cmdUI.showTable(arrrayCommand);
                    break;
                default:
                    System.out.println("Введите правильно команду. (help - вывод списка команд)");
                    break;
            }
        }
    }

    private void showTable(String[] arrrayCommand) {
        String tableName = arrrayCommand[1];
        if (!dbManager.isTableExist(tableName)){
            System.out.println("Нет такой таблицы. Доступны таблицы:");
            System.out.println(dbManager.getTableNames());
            return;
        }

        if (arrrayCommand.length == 2) {
            dbManager.showTable(tableName);
        } else if (arrrayCommand.length == 4) {
            int limit = Integer.valueOf(arrrayCommand[2]);
            int offset = Integer.valueOf(arrrayCommand[3]);
            dbManager.showTable(tableName, limit, offset);
        }
    }

    public void connectDB() {
        while (dbManager.getConnection() == null) {
            System.out.print("Введите название базы данных(sqlcmd): ");
            dbName = in.nextLine();
            System.out.print("Введите имя пользователя: ");
            userName = in.nextLine();
            System.out.print("Введите пароль: ");
            userPass = in.nextLine();
            dbManager.connect(dbName, userName, userPass);
            if (dbManager.getConnection() == null) {
                System.out.println("Повторить попытку? (yes/no):");
                String input = in.nextLine();
                if (!input.equals("yes")) {
                    return;
                }
            }
        }
        System.out.println("Подключение к базе данных выполнено");
    }

    public CmdUI() {
        in = new Scanner(System.in);
    }
}
