package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.command.Command;
import ua.com.juja.sqlcmd.controller.command.util.InputLine;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Vitalii Viazovoi on 13.04.2016.
 */
public class Unsupported extends Command {

    public Unsupported(View view) {
        super(view);
        description = null;
        formats = null;
    }

    @Override
    public boolean canProcess(InputLine line) {
        return true;
    }

    @Override
    public void process(InputLine line) {
        view.write("Введите правильно команду. (help - вывод списка команд)");
    }
}
