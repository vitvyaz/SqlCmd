package ua.com.juja.vitvyaz.sqlcmd.controller.command;

import ua.com.juja.vitvyaz.sqlcmd.controller.command.util.InputLine;
import ua.com.juja.vitvyaz.sqlcmd.model.DatabaseManager;
import ua.com.juja.vitvyaz.sqlcmd.view.View;

/**
 * Created by Vitalii Viazovoi on 23.04.2016.
 */
public class Drop extends Command {

    public Drop(View view, DatabaseManager dbManager) {
        super(view, dbManager);
        description = "\tdrop tableName\n" +
                "\t\tудалить таблицу";
        formats = new String[] {"drop tableName"};
    }

    @Override
    public boolean canProcess(InputLine line) {
        return line.getWord(0).equals("drop");
    }

    @Override
    public void process(InputLine line) {
        if (formats != null) {
            line.parametersNumberValidation(formats);
        }

        String tableName = line.getWord(1);
        line.tableNameValidation(dbManager, tableName);

        view.write(String.format("Удаляем таблицу '%s'! Для подтверждения введите 'yes':", tableName));
        String input = view.read();
        if (!input.equals("yes")) {
            view.write("Удаление таблицы отменено");
            return;
        }

        dbManager.dropTable(tableName);
        view.write(String.format("Таблица %s удалена", tableName));

    }
}
