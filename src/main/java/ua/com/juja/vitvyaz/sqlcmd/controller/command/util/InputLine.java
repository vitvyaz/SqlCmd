package ua.com.juja.vitvyaz.sqlcmd.controller.command.util;

import ua.com.juja.vitvyaz.sqlcmd.model.DatabaseManager;

/**
 * Created by Vitalii Viazovoi on 08.05.2016.
 */
public class InputLine {
    private String[] words;

    public InputLine(String line) {
        line = line.replaceAll("\\s+", " ");
        words = line.split(" ");
    }

    public String getWord(int i) {
        return words[i];
    }

    public int countWords() {
        return words.length;
    }

    public void parametersNumberValidation(String[] formats) {
        boolean numberNotOk = true;
        for (String format: formats) {
            if (countWords() == format.split(" ").length) {
                numberNotOk = false;
            }
        }
        if (numberNotOk) {
            String message = String.format("Ошибка! Введено неправильное количество параметров команды %s ", getWord(0));
            throw new IllegalArgumentException(message);
        }
    }

    public void tableNameValidation(DatabaseManager dbManager, String tableName) {
        if (!dbManager.existTable(tableName)) {
            throw new IllegalArgumentException("Ошибка! Нет такой таблицы. Доступны таблицы:\n" +
                    dbManager.getTableNames().toString());
        }
    }
}
