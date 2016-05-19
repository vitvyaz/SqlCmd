package ua.com.juja.vitvyaz.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.vitvyaz.sqlcmd.controller.command.util.InputLine;
import ua.com.juja.vitvyaz.sqlcmd.model.DatabaseManager;
import ua.com.juja.vitvyaz.sqlcmd.view.View;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Created by Vitalii Viazovoi on 07.05.2016.
 */
public class CreateTest {

    private View view;
    private DatabaseManager dbManager;
    private Command command;

    @Before
    public void setup() {
        view = mock(View.class);
        dbManager = mock(DatabaseManager.class);
        command = new Create(view, dbManager);
    }

    @Test
    public void testCanProcessCreateTable() {
        //when
        InputLine input = new InputLine("create test (id int PRIMARY KEY NOT NULL, name text, password text)");
        boolean canProcess = command.canProcess(input);

        //then
        assertTrue(canProcess);
    }


    @Test
    public void testCanProcessQweString() {
        //when
        InputLine input = new InputLine("qwe");
        boolean canProcess = command.canProcess(input);

        //then
        assertFalse(canProcess);
    }

    @Test
    public void testProcessCreateTable() {
        //given
        Command command = new Create(view, dbManager);

        //when
        InputLine input = new InputLine("create test (id int PRIMARY KEY NOT NULL, name text, password text)");
        command.process(input);

        //then
        Mockito.verify(view).write("Таблица test создана");

    }
}
