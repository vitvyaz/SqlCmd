package ua.com.juja.sqlcmd;

import java.io.Console;
import java.util.Scanner;

/**
 * Created by Vitalii Viazovoi on 03.03.2016.
 */
public class Proba {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Введите команду:");
        String q1 = in.next();
        System.out.println(q1);
        if (in.hasNextLine()) in.nextLine();
        System.out.println("Ввведите строку");
        String command = in.nextLine();
        System.out.println(command);

    }
}
