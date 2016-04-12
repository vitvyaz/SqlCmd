package ua.com.juja.sqlcmd.controller.command;

/**
 * Created by Vitalii Viazovoi on 12.04.2016.
 */
public interface Command {

    boolean canProcess(String command);

    void process(String[] arrayCommand);
}
