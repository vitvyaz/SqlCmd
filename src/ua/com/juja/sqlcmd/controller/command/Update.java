package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.JDBCPosgreManager;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Vitalii Viazovoi on 13.04.2016.
 */
public class Update implements Command {

    private View view;
    private DatabaseManager dbManager;

    public Update(View view, JDBCPosgreManager dbManager) {
        this.view = view;
        this.dbManager = dbManager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("update");
    }

    @Override
    public void process(String[] arrayCommand) {
        if (arrayCommand.length != 3) {
            view.write("Неправильное количество параметров для команды update. Должно быть 3");
            return;
        }

        String tableName = arrayCommand[1];
        if (!dbManager.isTableExist(tableName)) {
            view.write("Нет такой таблицы");
            return;
        }

        String rowId = arrayCommand[2];
        DataSet rowData = getAndWriteRowData(tableName, rowId);
        if (rowData == null) return;

        view.write("Введите данные к изменению в формате: field1 newValue1 field2 newValue2 ... ");
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
        DataSet condition = new DataSet();
        condition.add("id", rowId);
        dbManager.updateQuery(tableName, dataToChange, condition);
        view.write("Измененная строка:");
        getAndWriteRowData(tableName, rowId);
    }

    private DataSet getAndWriteRowData(String tableName, String rowId) {
        DataSet rowData = dbManager.getRow(tableName, rowId);
        if (rowData.size() == 0) {
            view.write("В таблице " + tableName + " нет строки с id: " + rowId);
            return null;
        }

        view.write(rowData.getTable());
        return rowData;
    }
}
