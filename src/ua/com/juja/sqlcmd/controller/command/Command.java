package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.command.util.InputLine;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Vitalii Viazovoi on 12.04.2016.
 */
public abstract class Command {
    protected String description;
    protected String[] formats;

    protected View view;
    protected DatabaseManager dbManager;

    public Command(View view, DatabaseManager dbManager) {
        this.view = view;
        this.dbManager = dbManager;
    }

    public Command(View view) {
        this.view = view;
    }

    public abstract boolean canProcess(InputLine line);

    public abstract void process(InputLine line);

    String getDescrition() {
        return description;
    }

    String[] getFormats() {
        return formats;
    }
}
