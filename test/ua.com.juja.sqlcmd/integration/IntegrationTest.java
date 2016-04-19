package ua.com.juja.sqlcmd.integration;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.Main;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

/**
 * Created by Vitalii Viazovoi on 16.04.2016.
 */
public class IntegrationTest {

    private static ConfigurabeInputStream in;
    private static ByteArrayOutputStream out;

    @BeforeClass
    public static void setup() {
        in = new ConfigurabeInputStream();
        out = new ByteArrayOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
    }


    @Test
    public void ConnectAndExitTest() {
        //given
        in.add("sqlcmd");
        in.add("postgres");
        in.add("postgres");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Введите название базы данных(sqlcmd): \r\n" +
                "Введите имя пользователя: \r\n" +
                "Введите пароль: \r\n" +
                "Подключение к базе данных выполнено\r\n" +
                "Введите команду:\r\n" +
                "До свидания!\r\n", getData());
    }

    public String getData() {
        try {
            return new String(out.toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
