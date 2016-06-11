package ua.com.juja.vitvyaz.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.vitvyaz.sqlcmd.controller.command.util.InputLine;
import ua.com.juja.vitvyaz.sqlcmd.model.DatabaseManager;
import ua.com.juja.vitvyaz.sqlcmd.view.View;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Vitalii Viazovoi on 07.05.2016.
 */
public class TablesTest {

    private View view;
    private DatabaseManager dbManager;
    private Command command;

    @Before
    public void setup() {
        view = mock(View.class);
        dbManager = mock(DatabaseManager.class);
        command = new Tables(view, dbManager);
    }

    @Test
    public void testCanProcessListString() {
        //given

        //when
        InputLine input = new InputLine("tables");
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
    public void testListTableNames() {
        //given
        Set<String> tableNames = new HashSet<>();
        tableNames.add("test");
        tableNames.add("users");
        when(dbManager.getTableNames()).thenReturn(tableNames);

        //when
        InputLine input = new InputLine("tables");
        command.process(input);

        //then
        verify(view).write("[test, users]");
    }

 }

