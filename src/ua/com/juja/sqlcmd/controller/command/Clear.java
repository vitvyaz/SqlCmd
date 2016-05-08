package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.command.util.InputLine;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Vitalii Viazovoi on 18.04.2016.
 */
public class Clear extends Command {

    public Clear(View view, DatabaseManager dbManager) {
        super(view, dbManager);
        description = "\tclear tableName" +
                "\t\tочистить содержимое таблицы";
        formats = new String[] {"clear tableName"};
    }

    @Override
    public boolean canProcess(InputLine line) {
        return line.getWord(0).equals("clear");
    }

    @Override
    public void process(InputLine line) {
        if (formats != null) {
            line.parametersNumberValidation(formats);
        }

        String tableName = line.getWord(1);
        line.tableNameValidation(dbManager, tableName);

        view.write(String.format("Удаляем все строки таблицы '%s'! Для подтверждения введите 'yes':", tableName));
        String input = view.read();
        if (!input.equals("yes")) {
            view.write("Удаление строк таблицы отменено");
            return;
        }

        dbManager.clearTable(tableName);
        view.write(String.format("Все строки в таблице %s удалены", tableName));

    }

}
