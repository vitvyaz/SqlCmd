package ua.com.juja.sqlcmd.controller.command;

import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.sqlcmd.controller.command.util.InputLine;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.*;

/**
 * Created by Vitalii Viazovoi on 07.05.2016.
 */
public class ExitTest {

    private View view = Mockito.mock(View.class);

    @Test
    public void canProcessExitStringTest() {
        //given
        Command command = new Exit(view);

        //when
        InputLine input = new InputLine("exit");
        boolean canProcess = command.canProcess(input);

        //then
        assertTrue(canProcess);
    }


    @Test
    public void canProcessQweStringTest() {
        //given
        Command command = new Exit(view);

        //when
        InputLine input = new InputLine("qwe");
        boolean canProcess = command.canProcess(input);

        //then
        assertFalse(canProcess);
    }


    @Test
    public void processCommandExitTest() {
        //given
        Command command = new Exit(view);

        //when
        InputLine input = new InputLine("exit");
        command.process(input);

        //then
        Mockito.verify(view).write("До свидания!");

    }
}
