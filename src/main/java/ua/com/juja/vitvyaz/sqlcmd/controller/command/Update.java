package ua.com.juja.vitvyaz.sqlcmd.controller.command;

import ua.com.juja.vitvyaz.sqlcmd.controller.command.util.InputLine;
import ua.com.juja.vitvyaz.sqlcmd.model.DataSet;
import ua.com.juja.vitvyaz.sqlcmd.model.DatabaseManager;
import ua.com.juja.vitvyaz.sqlcmd.model.TableBuilder;
import ua.com.juja.vitvyaz.sqlcmd.view.View;

/**
 * Created by Vitalii Viazovoi on 13.04.2016.
 */
public class Update extends Command {

    public Update(View view, DatabaseManager dbManager) {
        super(view, dbManager);
        description = "\tupdate tableName ID\n" +
                "\t\tизменить строку таблицы tableName (ID - идентификатор строки)";
        formats = new String[] {"update tableName ID"};
    }

    @Override
    public boolean canProcess(InputLine line) {
        return line.getWord(0).equals("update");
    }

    @Override
    public void process(InputLine line) {
        if (formats != null) {
            line.parametersNumberValidation(formats);
        }

        String tableName = line.getWord(1);
        line.tableNameValidation(dbManager, tableName);

        String rowId = line.getWord(2);
        DataSet rowData = getAndWriteRowData(tableName, rowId);
        if (rowData == null) return;

        view.write("Введите данные к изменению в формате: field1 newValue1 field2 newValue2 ... ");
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
        DataSet condition = new DataSet();
        condition.add("id", rowId);
        dbManager.update(tableName, dataToChange, condition);
        view.write("Измененная строка:");
        getAndWriteRowData(tableName, rowId);
    }

    private DataSet getAndWriteRowData(String tableName, String rowId) {
        DataSet rowData = dbManager.getRow(tableName, rowId);
        if (rowData.size() == 0) {
            throw new IllegalArgumentException("В таблице " + tableName + " нет строки с id: " + rowId);
        }

        view.write(new TableBuilder(rowData).toString());
        return rowData;
    }
}
