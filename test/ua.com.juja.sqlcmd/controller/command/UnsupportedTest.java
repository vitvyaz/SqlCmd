package ua.com.juja.sqlcmd.controller.command;

import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.sqlcmd.controller.command.util.InputLine;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Vitalii Viazovoi on 07.05.2016.
 */
public class UnsupportedTest {

    private View view = Mockito.mock(View.class);

    @Test
    public void testCanProcessQweString() {
        //given
        Command command = new Unsupported(view);

        //when
        InputLine input = new InputLine("qwe");
        boolean canProcess = command.canProcess(input);

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testProcessCommandExit() {
        //given
        Command command = new Unsupported(view);

        //when
        InputLine input = new InputLine("exit");
        command.process(input);

        //then
        Mockito.verify(view).write("Введите правильно команду. (help - вывод списка команд)");

    }
}
