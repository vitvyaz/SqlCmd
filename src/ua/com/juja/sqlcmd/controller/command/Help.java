package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.command.util.InputLine;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Vitalii Viazovoi on 13.04.2016.
 */
public class Help extends Command {

    public Help(View view) {
        super(view);
        description = "\thelp" +
                "\t\tвывести список команд";
        formats = new String[] {"help"};
    }

    @Override
    public boolean canProcess(InputLine line) {
        return line.getWord(0).equals("help");
    }

    @Override
    public void process(InputLine line) {
        view.write("Существующие команды:");
        view.write("\thelp");
        view.write("\t\tвывести список команд");
        view.write("\tlist");
        view.write("\t\tвывести список таблиц");
        view.write("\tfind tableName [LIMIT OFFET] ");
        view.write("\t\tвывести содержимое таблицы [LIMIT - количество строк OFFSET - начальная строка]");
        view.write("\tclear tableName");
        view.write("\t\tочистить содержимое таблицы");
        view.write("\tdrop tableName");
        view.write("\t\tудалить таблицу");
        view.write("\tcreate tableName ( columnName1 dataType1 [PRIMARY KEY] [NOT NULL], ... comumnNameN dataTypeN [NOT NULL] )");
        view.write("\t\tсоздать таблицу");
        view.write("\tinsert tableName");
        view.write("\t\tвставить строку в таблицу");
        view.write("\tupdate tableName ID");
        view.write("\t\tизменить строку таблицы tableName (ID - идентификатор строки)");
        view.write("\texit");
        view.write("\t\tвыход из программы");
    }
}
