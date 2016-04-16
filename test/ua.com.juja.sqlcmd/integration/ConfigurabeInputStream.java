package ua.com.juja.sqlcmd.integration;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Vitalii Viazovoi on 16.04.2016.
 */
public class ConfigurabeInputStream extends InputStream {

    private String line;

    @Override
    public int read() throws IOException {

        if (line.length() == 0) {
            return  -1;
        }

        char ch = line.charAt(0);
        line = line.substring(1);
        return (int)ch;
    }

    public void add(String addedLine) {
        if (line == null) {
            line = addedLine;
        } else {
            line += "\n" + addedLine;
        }
    }
}

