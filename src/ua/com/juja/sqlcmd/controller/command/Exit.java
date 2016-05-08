package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.command.util.InputLine;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Vitalii Viazovoi on 12.04.2016.
 */
public class Exit extends Command {

    public Exit(View view) {
        super(view);
        description = "\texit" +
                "\t\tвыход из программы";
        formats = new String[] {"exit"};
    }

    @Override
    public boolean canProcess(InputLine line) {
        return line.getWord(0).equals("exit");
    }

    @Override
    public void process(InputLine line) {
        view.write("До свидания!");
    }
}
