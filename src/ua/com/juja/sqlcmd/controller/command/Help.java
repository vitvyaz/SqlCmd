package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.command.util.InputLine;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Vitalii Viazovoi on 13.04.2016.
 */
public class Help extends Command {
    private Command[] commands;

    public Help(View view) {
        super(view);
        description = "\thelp\n" +
                "\t\tвывести список команд";
        formats = new String[] {"help"};
    }

    @Override
    public boolean canProcess(InputLine line) {
        return line.getWord(0).equals("help");
    }

    @Override
    public void process(InputLine line) {//todo переделать
        view.write("Существующие команды:");
        for (Command item: commands) {
            if(item.description != null) {
                view.write(item.description);
            }
        }
    }

    public void setCommands(Command[] commands) {
        this.commands = commands;
    }
}
