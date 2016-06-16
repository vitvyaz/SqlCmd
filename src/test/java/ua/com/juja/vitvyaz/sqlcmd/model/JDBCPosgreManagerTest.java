package ua.com.juja.vitvyaz.sqlcmd.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Vitalii Viazovoi on 01.03.2016.
 */
public class JDBCPosgreManagerTest {

    private static Set<String> tablesWithoutTableTest = new HashSet<>();
    private JDBCPosgreManager dbManager;

    @BeforeClass
    public static void init() {
        DatabaseManager dbManager = new JDBCPosgreManager();
        dbManager.connect("sqlcmd", "postgres", "postgres");
        dbManager.dropTable("test");
        tablesWithoutTableTest = dbManager.getTableNames();
        dbManager.createTable("test (id int PRIMARY KEY NOT NULL, name text, password text)");
    }

    @Before
    public void setup() {
        dbManager = new JDBCPosgreManager();
    }


    @Test
    public void testGetTableColumns() {
        dbManager.connect("sqlcmd", "postgres", "postgres");
        Set<String> tableColumns = dbManager.getTableColumns("test");
        assertEquals("[id, name, password]", tableColumns.toString());
    }

    @Test
    public void testGetTableColumnsWrongTableName() {
        dbManager.connect("sqlcmd", "postgres", "postgres");
        try {
            Set<String> tableColumns = dbManager.getTableColumns("wrongtable");
        } catch (IllegalArgumentException e) {
            assertEquals("Ошибка! Нет таблицы с именем: wrongtable", e.getMessage());
        }
    }

    @Test
    public void testClearTable() {
        dbManager.connect("sqlcmd", "postgres", "postgres");

        DataSet input = new DataSet();
        input.add("id", 13);
        input.add("name", "Stiven");
        input.add("password", "pass");
        dbManager.insertRow("test", input);
        assertEquals("[[id, name, password]\n" +
                "[13, Stiven, pass]\n" +
                "]", dbManager.getTableData("test").toString());
        dbManager.clearTable("test");
        assertEquals("[]", dbManager.getTableData("test").toString());
    }

    @Test
    public void testClearTableWrongTableName() {
        dbManager.connect("sqlcmd", "postgres", "postgres");
        try {
            dbManager.clearTable("wrongtable");
            fail();
        } catch (Exception e) {
            assertEquals("Ошибка sql query: DELETE FROM wrongtable", e.getMessage());
        }
    }

    @Test
    public void testDisconect() {
        dbManager.connect("sqlcmd", "postgres", "postgres");
        dbManager.disconnect();
        assertFalse(dbManager.isConnected());
    }

    @Test
    public void testIsTableExist() {
        dbManager.connect("sqlcmd", "postgres", "postgres");
        assertTrue(dbManager.isTableExist("test"));
    }

    @Test
    public void testIsTableExistWrongTableName() {
        dbManager.connect("sqlcmd", "postgres", "postgres");
        assertFalse(dbManager.isTableExist("wrongtable"));
    }

    @Test
    public void testIsTableExistEmptyTableName() {
        dbManager.connect("sqlcmd", "postgres", "postgres");
        assertFalse(dbManager.isTableExist(""));
    }

    @Test
    public void testConnectWrongPassword() {
        try {
            dbManager.connect("sqlcmd", "postgres", "wrongPassword");
            fail();
        } catch (Exception e) {
            assertEquals("Не удается подключиться к базе данных: sqlcmd имя пользователя: postgres", e.getMessage());
        }
    }

    @Test
    public void testConnectWrongUser() {
        try {
            dbManager.connect("sqlcmd", "wronguser", "postgres");
            fail();
        } catch (RuntimeException e) {
            assertEquals("Не удается подключиться к базе данных: sqlcmd имя пользователя: wronguser", e.getMessage());
        }
    }

    @Test
    public void testConnectWrongDatabase() {
        try {
            dbManager.connect("sqlcmd1", "postgres", "postgres");
            fail();
        } catch (RuntimeException e) {
            assertEquals("Не удается подключиться к базе данных: sqlcmd1 имя пользователя: postgres", e.getMessage());
        }
    }

    @Test
    public void testIsConnectedWhenConnect() {
        dbManager.connect("sqlcmd", "postgres", "postgres");
        assertTrue(dbManager.isConnected());
    }

    @Test
    public void testIsConnectedWhenNotConnect() {
        assertFalse(dbManager.isConnected());
    }


    @Test
    public void testGetAllTableNames() {
        dbManager.connect("sqlcmd", "postgres", "postgres");
        assertEquals("[test, users]",dbManager.getTableNames().toString());
    }

    @Test
    public void testUpdateTableData() {
        // given
        connectAndClearTableUsers();

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
        connectAndClearTableUsers();

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

    private void connectAndClearTableUsers() {
        dbManager.connect("sqlcmd", "postgres", "postgres");
        dbManager.execQuery("DELETE FROM users");
    }
}
