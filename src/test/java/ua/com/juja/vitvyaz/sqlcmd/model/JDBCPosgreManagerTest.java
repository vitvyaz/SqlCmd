package ua.com.juja.vitvyaz.sqlcmd.model;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Vitalii Viazovoi on 01.03.2016.
 */
public class JDBCPosgreManagerTest {

    private JDBCPosgreManager dbManager;

    @Before
    public void setup() {
        dbManager = new JDBCPosgreManager();
        dbManager.connect("sqlcmd", "postgres", "postgres");
    }

    @Test
    public void testGetAllTableNames() {
        assertEquals("[test, users]",dbManager.getTableNames().toString());
    }

    @Test
    public void testUpdateTableData() {
        // given
        clearTableUsers();

        // when
        DataSet input = new DataSet();
        input.add("id", 13);
        input.add("name", "Stiven");
        input.add("password", "pass");
        dbManager.insertRow("users", input);

        // then
        DataSet dataToUpdate = new DataSet();
        dataToUpdate.add("name", "StivenPupkin");
        dataToUpdate.add("password", "PupkinPassword");

        DataSet condition = new DataSet();
        condition.add("id", 13);

        dbManager.updateQuery("users", dataToUpdate, condition);

        List<DataSet> users = dbManager.getTableData("users");
        DataSet user = users.get(0);
        assertEquals("[name, password, id]", user.getNames().toString());
        assertEquals("[StivenPupkin, PupkinPassword, 13]", user.getValues().toString());
    }

    @Test
    public void testInsertTableData() {
        // given
        clearTableUsers();

        // when
        DataSet input = new DataSet();
        input.add("id", 13);
        input.add("name", "Stiven");
        input.add("password", "pass");
        dbManager.insertRow("users", input);

        // then
        List<DataSet> users = dbManager.getTableData("users");
        assertEquals(1, users.size());

        DataSet user = users.get(0);
        assertEquals("[name, password, id]", user.getNames().toString());
        assertEquals("[Stiven, pass, 13]", user.getValues().toString());

        for (int i = 1; i < 13; i++) {
            input = new DataSet();
            input.add("id", i);
            input.add("name", "user" + i);
            input.add("password", "pass" + i);
            dbManager.insertRow("users", input);
        }
        users = dbManager.getTableData("users");
        assertEquals(13, users.size());
    }

    private void clearTableUsers() {
        dbManager.execQuery("DELETE FROM users");
    }
}
