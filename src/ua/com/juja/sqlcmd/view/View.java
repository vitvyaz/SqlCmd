package ua.com.juja.sqlcmd.view;

/**
 * Created by Vitalii Viazovoi on 09.03.2016.
 */
public interface View {

    void write(String message);

    String read();
}
