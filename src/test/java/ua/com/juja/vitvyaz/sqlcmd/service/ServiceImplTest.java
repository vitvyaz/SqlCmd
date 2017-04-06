package ua.com.juja.vitvyaz.sqlcmd.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.com.juja.vitvyaz.sqlcmd.model.DataSet;
import ua.com.juja.vitvyaz.sqlcmd.model.DatabaseManager;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * @author Vitalii Viazovoi
 */
@RunWith(MockitoJUnitRunner.class)
public class ServiceImplTest {

    private ServiceImpl serviceImpl = new ServiceImpl() {
        @Override
        protected DatabaseManager getManager() {
            return dbManager;
        }
    };

    @Mock
    private DatabaseManager dbManager;

    @Test
    public void testFind() throws ServiceException {
        //given
        DataSet row1 = new DataSet();
        row1.add("id", "1");
        row1.add("name", "Pupkin");
        row1.add("password", "Pass");
        DataSet row2 = new DataSet();
        row2.add("id", "2");
        row2.add("name", "Petrov");
        row2.add("password", "PassPass");

        when(dbManager.getTableColumns("users"))
                .thenReturn(new HashSet(Arrays.asList("id", "name", "password")));
        when(dbManager.getTableData("users")).thenReturn(Arrays.asList(row1, row2));

        //when
        List<List<String>> users = serviceImpl.find(dbManager, "users");

        //then
        assertEquals("[[password, name, id], " +
                    "[Pass, Pupkin, 1], " +
                    "[PassPass, Petrov, 2]]",
                users.toString());
    }

    @Test
    public void testFindException() {
        //given
        when(dbManager.getTableColumns("users")).thenThrow(new RuntimeException("error message"));
        //when

        //then
        try {
            serviceImpl.find(dbManager, "users");
            fail();
        } catch (ServiceException e) {
            assertEquals("Command find error: error message",e.getMessage());
        }
    }

    @Test
    public void testConnect() throws ServiceException {
        //given

        //when
        DatabaseManager expectedDbManager = serviceImpl.connect("database", "user", "password");

        //then
        assertEquals(expectedDbManager, dbManager);
    }

    @Test
    public void testConnectException(){
        //given
        doThrow(new RuntimeException("exception's message")).when(dbManager).connect("database", "user", "password");

        //when
        try {
        DatabaseManager expectedDbManager = serviceImpl.connect("database", "user", "password");
            fail();
        } catch (ServiceException e) {
            assertEquals("Connection error: exception's message",e.getMessage());;
        }

        //then

    }

    @Test
    public void testCommandList() {
        //given
        //when
        List<String> commands = Arrays.asList("help", "connect", "tables", "create");
        //then
        assertEquals(serviceImpl.commandsList(), commands);
    }

    @Test
    public void testTables() throws ServiceException {
        //given
        when(dbManager.getTableNames()).thenReturn(new LinkedHashSet(Arrays.asList("table1", "table2")));

        //when
        Set<String> tables = serviceImpl.tables(dbManager);
        //then
        assertEquals("[table1, table2]", tables.toString());
    }

    @Test
    public void testTablesException() {
        //given
        when(dbManager.getTableNames()).thenThrow(new RuntimeException("error message"));
        //when

        //then
        try {
            serviceImpl.tables(dbManager);
            fail();
        } catch (ServiceException e) {
            assertEquals("Command tables error: error message",e.getMessage());
        }
    }

    @Test
    public void testColumns() throws ServiceException {
        //given
        when(dbManager.getTableColumns("users"))
                .thenReturn(new LinkedHashSet(Arrays.asList("id", "name", "password")));

        //when
        List<String> columns = serviceImpl.columns(dbManager, "users");

        //then
        assertEquals("[id, name, password]", columns.toString());
    }

    @Test
    public void testColumnsException() {
        //given
        when(dbManager.getTableColumns("users")).thenThrow(new RuntimeException("error message"));
        //when

        //then
        try {
            serviceImpl.columns(dbManager, "users");
            fail();
        } catch (ServiceException e) {
            assertEquals("Command columns error: error message",e.getMessage());
        }
    }
}
