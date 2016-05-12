package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.sqlcmd.controller.command.util.InputLine;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Vitalii Viazovoi on 07.05.2016.
 */
public class UpdateTest {

    private View view;
    private DatabaseManager dbManager;
    private Command command;

    @Before
    public void setup() {
        view = mock(View.class);
        dbManager = mock(DatabaseManager.class);
        command = new Update(view, dbManager);

    }

    @Test
    public void testCanProcessUpdateUsersId1String() {
        //given

        //when
        InputLine input = new InputLine("update users 1");
        boolean canProcess = command.canProcess(input);

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessQweString() {
        //given

        //when
        InputLine input = new InputLine("qwe");
        boolean canProcess = command.canProcess(input);

        //then
        assertFalse(canProcess);
    }


    @Test
    public void testUpdateRowData() {
        //given
        DataSet currentRow = new DataSet();
        currentRow.add("id", "12");
        currentRow.add("name", "Vasya");
        currentRow.add("password", "1111");
        DataSet changedRow = new DataSet();
        changedRow.add("id", "12");
        changedRow.add("name", "Ivan");
        changedRow.add("password", "*****");

        when(dbManager.getRow("users", "12")).thenReturn(currentRow).thenReturn(changedRow);

        when(dbManager.isTableExist("users")).thenReturn(true);

        when(view.read()).thenReturn("name Ivan password *****");

        //when
        InputLine input = new InputLine("update users 12");
        command.process(input);

        //then

        verify(view).read();
        shouldPrint(
                "[-------------------------\n" +
                        "| id |  name | password |\n" +
                        "-------------------------\n" +
                        "| 12 | Vasya |     1111 |\n" +
                        "-------------------------, Введите данные к изменению в формате: field1 newValue1 field2 newValue2 ... ," +
                        " Измененная строка:, " +
                        "------------------------\n" +
                        "| id | name | password |\n" +
                        "------------------------\n" +
                        "| 12 | Ivan |    ***** |\n" +
                        "------------------------]");
    }

    @Test
    public void testUpdateInputWrongNumberOfNewValues() {
        //given
        DataSet currentRow = new DataSet();
        currentRow.add("id", "12");
        currentRow.add("name", "Vasya");
        currentRow.add("password", "1111");

        when(dbManager.getRow("users", "12")).thenReturn(currentRow);

        when(dbManager.isTableExist("users")).thenReturn(true);

        when(view.read()).thenReturn("name Ivan password");

        //when
        InputLine input = new InputLine("update users 12");
        try {
            command.process(input);
            fail();
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Ошибка! Нечетное количество параметров", e.getMessage());
        }

        verify(view).read();
        shouldPrint(
                "[-------------------------\n" +
                        "| id |  name | password |\n" +
                        "-------------------------\n" +
                        "| 12 | Vasya |     1111 |\n" +
                        "-------------------------, Введите данные к изменению в формате: field1 newValue1 field2 newValue2 ... ]");
    }

    @Test
    public void testUpdateErrorRowIdNotExist() {
        //given
        when(dbManager.getRow("users", "99")).thenReturn(new DataSet());
        when(dbManager.isTableExist("users")).thenReturn(true);

        //when
        InputLine input = new InputLine("update users 99");
        try {
            command.process(input);
            fail();
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("В таблице users нет строки с id: 99", e.getMessage());
        }
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

}
