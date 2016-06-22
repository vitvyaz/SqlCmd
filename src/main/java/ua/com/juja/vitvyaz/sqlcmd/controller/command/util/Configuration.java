package ua.com.juja.vitvyaz.sqlcmd.controller.command.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Vitalii Viazovoi on 22.06.2016.
 */
public class Configuration {
    private static final String PROPERTIES_FILE = "src/main/resources/sqlcmd.properties";
    private Properties properties;

    private String driver;
    private String serverName;
    private String portNumber;

    public Configuration() {
        loadProperties();
    }

    public void loadProperties() {
        properties = new Properties();
        File file = new File(PROPERTIES_FILE);
        try (FileInputStream fileInput = new FileInputStream(file)) {
            properties.load(fileInput);
            driver = properties.getProperty("database.driver");
            serverName = properties.getProperty("database.server.name");
            portNumber = properties.getProperty("database.port");
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки properties из файла " + file.getAbsolutePath());
        }
    }

    public String getDriver() {
        return driver;
    }

    public String getServerName() {
        return serverName;
    }

    public String getPortNumber() {
        return portNumber;
    }
}
