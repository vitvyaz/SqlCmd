package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Vitalii Viazovoi on 13.04.2016.
 */
public class Help implements Command {

    View view;

    public Help(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("help");
    }

    @Override
    public void process(String[] arrayCommand) {
        view.write("Существующие команды:");
        view.write("\thelp");
        view.write("\t\tвывести список команд");
        view.write("\tlist");
        view.write("\t\tвывести список таблиц");
        view.write("\tfind tableName [LIMIT OFFET] ");
        view.write("\t\tвывести содержимое таблицы [LIMIT - количество строк OFFSET - начальная строка]");
        view.write("\tclear tableName");
        view.write("\t\tочистить содержимое таблицы");
        view.write("\tinsert tableName");
        view.write("\t\tвставить строку в таблицу");
        view.write("\tupdate tableName ID");
        view.write("\t\tизменить строку таблицы tableName (ID - идентификатор строки)");
        view.write("\texit");
        view.write("\t\tвыход из программы");
    }
}
