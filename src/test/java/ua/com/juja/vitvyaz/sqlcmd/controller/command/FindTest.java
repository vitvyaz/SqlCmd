package ua.com.juja.vitvyaz.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import ua.com.juja.vitvyaz.sqlcmd.controller.command.util.InputLine;
import ua.com.juja.vitvyaz.sqlcmd.model.DataSet;
import ua.com.juja.vitvyaz.sqlcmd.model.DatabaseManager;
import ua.com.juja.vitvyaz.sqlcmd.view.View;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Vitalii Viazovoi on 07.05.2016.
 */
public class FindTest {

    private View view;
    private DatabaseManager dbManager;
    private Command command;

    @Before
    public void setup() {
        view = mock(View.class);
        dbManager = mock(DatabaseManager.class);
        command = new Find(view, dbManager);
    }

    @Test
    public void testPrintTableData() {
        //given
        DataSet user1 = new DataSet();
        user1.add("id", "1");
        user1.add("name", "Vasya");
        user1.add("password", "1111");
        DataSet user2 = new DataSet();
        user2.add("id", "2");
        user2.add("name", "Petya");
        user2.add("password", "22222");
        ArrayList<DataSet> tableData = new ArrayList<>();
        tableData.add(user1);
        tableData.add(user2);
        when(dbManager.getTableData("users")).thenReturn(tableData);

        Set<String> tableColumns = new LinkedHashSet<>();
        tableColumns.add("id");
        tableColumns.add("name");
        tableColumns.add("password");
        when(dbManager.getTableColumns("users")).thenReturn(tableColumns);

        when(dbManager.existTable("users")).thenReturn(true);

        //when
        InputLine input = new InputLine("find users");
        command.process(input);

        //then
        shouldPrint(
                "[-------------------------\n" +
                "| id |  name | password |\n" +
                "-------------------------\n" +
                "|  1 | Vasya |     1111 |\n" +
                "|  2 | Petya |    22222 |\n" +
                "-------------------------]");
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected,captor.getAllValues().toString());
    }

    @Test
    public void testCanProcessExitString() {
        //given

        //when
        InputLine input = new InputLine("find users");
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
    public void testPrintEmptyTableData() {
        //given
        ArrayList<DataSet> tableData = new ArrayList<>();
        when(dbManager.getTableData("users")).thenReturn(tableData);

        Set<String> tableColumns = new LinkedHashSet<>();
        tableColumns.add("id");
        tableColumns.add("name");
        tableColumns.add("password");
        when(dbManager.getTableColumns("users")).thenReturn(tableColumns);

        when(dbManager.existTable("users")).thenReturn(true);

        //when
        InputLine input = new InputLine("find users");
        command.process(input);

        //then
        shouldPrint(
                "[------------------------\n" +
                "| id | name | password |\n" +
                "------------------------]");
    }

    @Test
    public void testPrintTableDataWithOffSet() {
        //given
        DataSet user1 = new DataSet();
        user1.add("id", "1");
        user1.add("name", "Vasya");
        user1.add("password", "1111");
        DataSet user2 = new DataSet();
        user2.add("id", "2");
        user2.add("name", "Petya");
        user2.add("password", "22222");
        ArrayList<DataSet> tableData = new ArrayList<>();
        tableData.add(user1);
        tableData.add(user2);
        when(dbManager.getTableData("users", 2 , 2)).thenReturn(tableData);

        Set<String> tableColumns = new LinkedHashSet<>();
        tableColumns.add("id");
        tableColumns.add("name");
        tableColumns.add("password");
        when(dbManager.getTableColumns("users")).thenReturn(tableColumns);

        when(dbManager.existTable("users")).thenReturn(true);

        //when
        InputLine input = new InputLine("find users 2 2");
        command.process(input);

        //then
        shouldPrint(
                "[-------------------------\n" +
                "| id |  name | password |\n" +
                "-------------------------\n" +
                "|  1 | Vasya |     1111 |\n" +
                "|  2 | Petya |    22222 |\n" +
                "-------------------------]");
    }

    @Test
    public void testFindErrorWithTwoParameters() {
        //given

        //when
        InputLine input = new InputLine("find users 2");
        try {
            command.process(input);
            fail();
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Ошибка! Введено неправильное количество параметров команды find ", e.getMessage());
        }
    }

    @Test
    public void testFindErrorWithFourParameters() {
        //given

        //when
        InputLine input = new InputLine("find users 2 qwe asd");
        try {
            command.process(input);
            fail();
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Ошибка! Введено неправильное количество параметров команды find ", e.getMessage());
        }
    }

    @Test
    public void testFindWithWrongTableName() {
        //given

        when(dbManager.existTable("wrongTableName")).thenReturn(false);
        Set<String> tableNames = new HashSet<>();
        tableNames.add("test");
        tableNames.add("users");
        when(dbManager.getTableNames()).thenReturn(tableNames);

        //when
        InputLine input = new InputLine("find wrongTableName");
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

