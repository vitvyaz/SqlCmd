package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.command.util.InputLine;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Vitalii Viazovoi on 18.04.2016.
 */
public class Insert extends Command {

    public Insert(View view, DatabaseManager dbManager) {
        super(view, dbManager);
        description = "\tinsert tableName" +
                "\t\tвставить строку в таблицу";
        formats = new String[] {"insert tableName"};
    }

    @Override
    public boolean canProcess(InputLine line) {
        return line.getWord(0).equals("insert");
    }

    @Override
    public void process(InputLine line) {
        if (formats != null) {
            line.parametersNumberValidation(formats);
        }

        String tableName = line.getWord(1);
        line.tableNameValidation(dbManager, tableName);

        view.write("Введите данные в формате: field1 newValue1 field2 newValue2 ... ");
        String fieldsValues = view.read();
        fieldsValues = fieldsValues.replaceAll("\\s+", " ");
        String[] arrayFieldValues = fieldsValues.split(" ");

        if (arrayFieldValues.length % 2 == 1) {
            throw new IllegalArgumentException("Ошибка! Нечетное количество параметров");
        }

        DataSet dataToChange = new DataSet();
        for (int i = 0; i < arrayFieldValues.length; i += 2) {
            dataToChange.add(arrayFieldValues[i], arrayFieldValues[i + 1]);
        }

        dbManager.insertRow(tableName, dataToChange);
        view.write("Строка добавлена");
    }
}
