package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;

import ua.com.juja.sqlcmd.controller.command.util.InputLine;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by Vitalii Viazovoi on 07.05.2016.
 */
public class FindTest {

    private View view;
    private DatabaseManager dbManager;

    @Before
    public void setup() {
        view = mock(View.class);
        dbManager = mock(DatabaseManager.class);

    }

    @Test
    public void printTableDataTest() {
        //given
        Command command = new Find(view, dbManager);

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

        ArrayList<String> tableColumns = new ArrayList<>();
        tableColumns.add("id");
        tableColumns.add("name");
        tableColumns.add("password");
        when(dbManager.getTableColumns("users")).thenReturn(tableColumns);

        when(dbManager.isTableExist("users")).thenReturn(true);

        //when
        InputLine input = new InputLine("find users");
        command.process(input);

        //then

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(
                "[-------------------------," +
                " | id |  name | password |," +
                " -------------------------," +
                " |  1 | Vasya |     1111 |," +
                " |  2 | Petya |    22222 |]",captor.getAllValues().toString());
    }
}
