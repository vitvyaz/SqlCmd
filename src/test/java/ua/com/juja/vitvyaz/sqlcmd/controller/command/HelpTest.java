package ua.com.juja.vitvyaz.sqlcmd.controller.command;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ua.com.juja.vitvyaz.sqlcmd.controller.command.util.InputLine;
import ua.com.juja.vitvyaz.sqlcmd.model.DatabaseManager;
import ua.com.juja.vitvyaz.sqlcmd.model.JDBCPosgreManager;
import ua.com.juja.vitvyaz.sqlcmd.view.View;

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
        Help command = new Help(view);
        DatabaseManager dbManager = new JDBCPosgreManager();
        Command[] commands = new Command[]{
                command,
                new Exit(view),
                new Tables(view, dbManager),
                new Find(view, dbManager),
                new Update(view, dbManager),
                new Insert(view, dbManager),
                new Clear(view, dbManager),
                new Create(view, dbManager),
                new Drop(view, dbManager),
                new Unsupported(view)
        };
        command.setCommands(commands);

        //when
        InputLine input = new InputLine("help");
        command.process(input);

        //then
        String expected =
                        "[Существующие команды:," +
                        " 	help\n" +
                        "\t\tвывести список команд," +
                        " 	exit\n" +
                        "\t\tвыход из программы," +
                        " 	tables\n" +
                        "\t\tвывести список таблиц," +
                        " 	find tableName [LIMIT OFFET]\n" +
                        "\t\tвывести содержимое таблицы [LIMIT - количество строк OFFSET - начальная строка]," +
                        " 	update tableName ID\n" +
                        "\t\tизменить строку таблицы tableName (ID - идентификатор строки)," +
                        " 	insert tableName\n" +
                        "\t\tвставить строку в таблицу," +
                        " 	clear tableName\n" +
                        "\t\tочистить содержимое таблицы," +
                        " 	create tableName ( columnName1 dataType1 [PRIMARY KEY] [NOT NULL], ..." +
                                " columnNameN dataTypeN [NOT NULL] )\n" +
                        "\t\tсоздать таблицу," +
                        " 	drop tableName\n" +
                        "\t\tудалить таблицу]";

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view, Mockito.atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }
}
