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

}
