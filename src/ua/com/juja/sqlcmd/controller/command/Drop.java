package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Vitalii Viazovoi on 23.04.2016.
 */
public class Drop implements Command {
    private View view;
    private DatabaseManager dbManager;

    public Drop(View view, DatabaseManager dbManager) {
        this.view = view;
        this.dbManager = dbManager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("drop");
    }

    @Override
    public void process(String[] arrayCommand) {
        if (arrayCommand.length < 2) {
            view.write("Не введено имя таблицы");
            return;
        }

        String tableName = arrayCommand[1];
        if (!dbManager.isTableExist(tableName)) {
            view.write("Нет такой таблицы. Доступны таблицы:");
            view.write(dbManager.getTableNames().toString());
            return;
        }

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
