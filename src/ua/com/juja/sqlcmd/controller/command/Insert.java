package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Vitalii Viazovoi on 18.04.2016.
 */
public class Insert implements Command {
    private View view;
    private DatabaseManager dbManager;

    public Insert(View view, DatabaseManager dbManager) {
        this.view = view;
        this.dbManager = dbManager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("insert");
    }

    @Override
    public void process(String[] arrayCommand) {
        if (arrayCommand.length < 2) {
            view.write("Не введено имя таблицы");
            return;
        }

        String tableName = arrayCommand[1];
        if (!dbManager.isTableExist(tableName)) {
            view.write("Нет такой таблицы");
            return;
        }

        view.write("Введите данные в формате: field1 newValue1 field2 newValue2 ... ");
        String fieldsValues = view.read();
        fieldsValues = fieldsValues.replaceAll("\\s+", " ");
        String[] arrayFieldValues = fieldsValues.split(" ");

        if (arrayFieldValues.length % 2 == 1) {
            view.write("Ошибка! Нечетное количество параметров");
            return;
        }

        DataSet dataToChange = new DataSet();
        for (int i = 0; i < arrayFieldValues.length; i += 2) {
            dataToChange.add(arrayFieldValues[i], arrayFieldValues[i + 1]);
        }

        dbManager.insertRow(tableName, dataToChange);
        view.write("Строка добавлена");
    }
}
