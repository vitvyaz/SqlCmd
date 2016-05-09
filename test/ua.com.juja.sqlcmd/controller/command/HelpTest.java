package ua.com.juja.sqlcmd.controller.command;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ua.com.juja.sqlcmd.controller.command.util.InputLine;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Vitalii Viazovoi on 07.05.2016.
 */
public class HelpTest {

    private View view = Mockito.mock(View.class);

    @Test
    public void testCanProcessHelpString() {
        //given
        Command command = new Help(view);

        //when
        InputLine input = new InputLine("help");
        boolean canProcess = command.canProcess(input);

        //then
        assertTrue(canProcess);
    }


    @Test
    public void testCanProcessQweString() {
        //given
        Command command = new Help(view);

        //when
        InputLine input = new InputLine("qwe");
        boolean canProcess = command.canProcess(input);

        //then
        assertFalse(canProcess);
    }

    @Test
    public void testProcessCommandHelp() {
        //given
        Command command = new Help(view);

        //when
        InputLine input = new InputLine("help");
        command.process(input);

        //then
        String expected =
                        "[Существующие команды:," +
                        " 	help," +
                        " 		вывести список команд," +
                        " 	list," +
                        " 		вывести список таблиц," +
                        " 	find tableName [LIMIT OFFET] ," +
                        " 		вывести содержимое таблицы [LIMIT - количество строк OFFSET - начальная строка]," +
                        " 	clear tableName," +
                        " 		очистить содержимое таблицы," +
                        " 	drop tableName," +
                        " 		удалить таблицу," +
                        " 	create tableName ( columnName1 dataType1 [PRIMARY KEY] [NOT NULL], ... comumnNameN dataTypeN [NOT NULL] )," +
                        " 		создать таблицу," +
                        " 	insert tableName," +
                        " 		вставить строку в таблицу," +
                        " 	update tableName ID," +
                        " 		изменить строку таблицы tableName (ID - идентификатор строки)," +
                        " 	exit," +
                        " 		выход из программы]";

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view, Mockito.atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }
}
