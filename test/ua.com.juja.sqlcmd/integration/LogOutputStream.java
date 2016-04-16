package ua.com.juja.sqlcmd.integration;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Vitalii Viazovoi on 16.04.2016.
 */
public class LogOutputStream extends OutputStream {

    private String log;

    @Override
    public void write(int b) throws IOException {
        log += String.valueOf((char) b);
    }

    public Object getData() {
        return log;
    }
}
