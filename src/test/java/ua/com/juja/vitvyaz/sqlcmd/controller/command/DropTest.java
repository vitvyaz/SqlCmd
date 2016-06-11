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
 * Created by Vitalii Viazovoi on 09.05.2016.
 */
public class DropTest {
    private View view;
    private DatabaseManager dbManager;
    private Command command;

    @Before
    public void setup() {
        view = mock(View.class);
        dbManager = mock(DatabaseManager.class);
        command = new Drop(view, dbManager);
    }

    @Test
    public void testCanProcessDropUsersString() {
        //given

        //when
        InputLine input = new InputLine("drop users");
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
    public void testDropTableUsersConfirmed() {
        //given
        when(view.read()).thenReturn("yes");
        when(dbManager.isTableExist("users")).thenReturn(true);

        //when
        InputLine input = new InputLine("drop users");
        command.process(input);

        //then
        verify(view).write("Удаляем таблицу 'users'! Для подтверждения введите 'yes':");
        verify(view).read();
        verify(view).write("Таблица users удалена");
    }

    @Test
    public void testDropTableUsersNotConfirmed() {
        //given
        when(view.read()).thenReturn("no");
        when(dbManager.isTableExist("users")).thenReturn(true);

        //when
        InputLine input = new InputLine("drop users");
        command.process(input);

        //then
        verify(view).write("Удаляем таблицу 'users'! Для подтверждения введите 'yes':");
        verify(view).read();
        verify(view).write("Удаление таблицы отменено");
    }

    @Test
    public void testDropWrongTableName() {
        //given
        when(view.read()).thenReturn("yes");
        when(dbManager.isTableExist("wrongTableName")).thenReturn(false);
        Set<String> tableNames = new HashSet<>();
        tableNames.add("test");
        tableNames.add("users");
        when(dbManager.getTableNames()).thenReturn(tableNames);

        //when
        InputLine input = new InputLine("drop wrongTableName");
        try {
            //then
            command.process(input);
            fail();
        } catch (IllegalArgumentException e) {
            String expectedMessage =
                    "Ошибка! Нет такой таблицы. Доступны таблицы:\n" +
                    "[test, users]";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testClearErrorWithoutParameters() {
        //given

        //when
        InputLine input = new InputLine("drop");
        try {
            command.process(input);
            fail();
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Ошибка! Введено неправильное количество параметров команды drop ", e.getMessage());
        }
    }

    @Test
    public void testClearErrorWithMoreThenTwoParameters() {
        //given

        //when
        InputLine input = new InputLine("drop");
        try {
            command.process(input);
            fail();
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Ошибка! Введено неправильное количество параметров команды drop ", e.getMessage());
        }
    }

}
