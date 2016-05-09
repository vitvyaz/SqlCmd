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
        DataSet rowData = new DataSet();
        rowData.add("id", "12");
        rowData.add("name", "Vasya");
        rowData.add("password", "1111");
        when(dbManager.getRow("users", "12")).thenReturn(rowData);

        when(dbManager.isTableExist("users")).thenReturn(true);

        when(view.read()).thenReturn("name Ivan password *****");

        //when
        InputLine input = new InputLine("update users 12");
        command.process(input);

        //then

        rowData = new DataSet();
        rowData.add("id", "12");
        rowData.add("name", "Ivan");
        rowData.add("password", "*****");
        verify(view).read();
        shouldPrint(
                "[-------------------------\n" +
                "| id |  name | password |\n" +
                "-------------------------\n" +
                "| 12 | Vasya |     1111 |\n" +
                "-------------------------, Введите данные к изменению в формате: field1 newValue1 field2 newValue2 ... ," +
                " Измененная строка:, " +
                "-------------------------\n" +
                "| id |  name | password |\n" +
                 "-------------------------\n" +
                "| 12 | Ivan |     ***** |\n" +
                "-------------------------]");
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

//    @Test
//    public void testInsertRowDataErrorNotPairParameters() {
//        //given
//        when(view.read()).thenReturn("id 1 name Ivan password");
//        when(dbManager.isTableExist("users")).thenReturn(true);
//
//        //when
//        InputLine input = new InputLine("insert users");
//        try {
//            command.process(input);
//            fail();
//            //then
//        } catch (IllegalArgumentException e) {
//            verify(view).write("Введите данные в формате: field1 newValue1 field2 newValue2 ... ");
//            verify(view).read();
//            assertEquals("Ошибка! Нечетное количество параметров", e.getMessage());
//        }
//    }
//
//    @Test
//    public void testInsertErrorWithMoreThenOneParameter() {
//        //given
//
//        //when
//        InputLine input = new InputLine("insert users 2");
//        try {
//            command.process(input);
//            fail();
//        } catch (IllegalArgumentException e) {
//            //then
//            assertEquals("Ошибка! Введено неправильное количество параметров команды insert ", e.getMessage());
//        }
//    }
//
//    @Test
//    public void testFindErrorWithoutParameters() {
//        //given
//
//        //when
//        InputLine input = new InputLine("insert");
//        try {
//            command.process(input);
//            fail();
//        } catch (IllegalArgumentException e) {
//            //then
//            assertEquals("Ошибка! Введено неправильное количество параметров команды insert ", e.getMessage());
//        }
//    }
//
//    @Test
//    public void testFindWithWrongTableName() {
//        //given
//
//        when(dbManager.isTableExist("wrongTableName")).thenReturn(false);
//        ArrayList<String> tableNames = new ArrayList<>();
//        tableNames.add("test");
//        tableNames.add("users");
//        when(dbManager.getTableNames()).thenReturn(tableNames);
//
//        //when
//        InputLine input = new InputLine("insert wrongTableName");
//        try {
//            command.process(input);
//            fail();
//        } catch (IllegalArgumentException e) {
//            //then
//            String expectedMessage =
//                    "Ошибка! Нет такой таблицы. Доступны таблицы:\n" +
//                    "[test, users]";
//            assertEquals(expectedMessage, e.getMessage());
//        }
//    }
 }

