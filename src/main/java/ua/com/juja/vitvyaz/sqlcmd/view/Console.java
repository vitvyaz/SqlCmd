package ua.com.juja.vitvyaz.sqlcmd.view;

import java.util.Scanner;

/**
 * Created by Vitalii Viazovoi on 09.03.2016.
 */
public class Console implements View {
    Scanner scanner;

    public Console() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void write(String message) {
        System.out.println(message);
    }

    @Override
    public String read() {
        return scanner.nextLine();
    }
}
