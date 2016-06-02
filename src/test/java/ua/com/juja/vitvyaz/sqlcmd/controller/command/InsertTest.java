package ua.com.juja.vitvyaz.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.vitvyaz.sqlcmd.controller.command.util.InputLine;
import ua.com.juja.vitvyaz.sqlcmd.model.DataSet;
import ua.com.juja.vitvyaz.sqlcmd.model.DatabaseManager;
import ua.com.juja.vitvyaz.sqlcmd.view.View;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Vitalii Viazovoi on 07.05.2016.
 */
public class InsertTest {

    private View view;
    private DatabaseManager dbManager;
    private Command command;

    @Before
    public void setup() {
        view = mock(View.class);
        dbManager = mock(DatabaseManager.class);
        command = new Insert(view, dbManager);
    }

    @Test
    public void testCanProcessInsertUsersString() {
        //given

        //when
        InputLine input = new InputLine("insert users");
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
    public void testInsertRowData() {
        //given
        when(view.read()).thenReturn("id 1 name Ivan password *****");
        when(dbManager.isTableExist("users")).thenReturn(true);

        //when
        InputLine input = new InputLine("insert users");
        command.process(input);

        //then
        verify(view).write("Введите данные в формате: field1 newValue1 field2 newValue2 ... ");
        verify(view).read();
        verify(view).write("Строка добавлена");
    }

    @Test
    public void testInsertRowDataErrorNotPairParameters() {
        //given
        when(view.read()).thenReturn("id 1 name Ivan password");
        when(dbManager.isTableExist("users")).thenReturn(true);

        //when
        InputLine input = new InputLine("insert users");
        try {
            command.process(input);
            fail();
            //then
        } catch (IllegalArgumentException e) {
            verify(view).write("Введите данные в формате: field1 newValue1 field2 newValue2 ... ");
            verify(view).read();
            assertEquals("Ошибка! Нечетное количество параметров", e.getMessage());
        }
    }

    @Test
    public void testInsertErrorWithMoreThenOneParameter() {
        //given

        //when
        InputLine input = new InputLine("insert users 2");
        try {
            command.process(input);
            fail();
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Ошибка! Введено неправильное количество параметров команды insert ", e.getMessage());
        }
    }

    @Test
    public void testFindErrorWithoutParameters() {
        //given

        //when
        InputLine input = new InputLine("insert");
        try {
            command.process(input);
            fail();
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Ошибка! Введено неправильное количество параметров команды insert ", e.getMessage());
        }
    }

    @Test
    public void testFindWithWrongTableName() {
        //given

        when(dbManager.isTableExist("wrongTableName")).thenReturn(false);
        ArrayList<String> tableNames = new ArrayList<>();
        tableNames.add("test");
        tableNames.add("users");
        when(dbManager.getTableNames()).thenReturn(tableNames);

        //when
        InputLine input = new InputLine("insert wrongTableName");
        try {
            command.process(input);
            fail();
        } catch (IllegalArgumentException e) {
            //then
            String expectedMessage =
                    "Ошибка! Нет такой таблицы. Доступны таблицы:\n" +
                    "[test, users]";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
 }

