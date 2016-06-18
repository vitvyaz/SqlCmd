package ua.com.juja.vitvyaz.sqlcmd.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
    public void testGetRowWrongID() {
        addRows();
        DataSet row = dbManager.getRow("test", "33");
        assertEquals("[]\n" +
                "[]\n", row.toString());
    }

    @Test
    public void testGetRow() {
        addRows();
        DataSet row = dbManager.getRow("test", "3");
        assertEquals("[id, name, password]\n" +
                "[3, user3, password3]\n", row.toString());
    }

    @Test
    public void testGetTableDataLimit3Offset3() {
        addRows();
        List<DataSet> data = dbManager.getTableData("test", 3, 3);
        assertEquals("[[id, name, password]\n" +
                "[3, user3, password3]\n" +
                ", [id, name, password]\n" +
                "[4, user4, password4]\n" +
                ", [id, name, password]\n" +
                "[5, user5, password5]\n" +
                "]", data.toString());
    }

    @Test
    public void testGetTableData() {
        addRows();
        List<DataSet> data = dbManager.getTableData("test");
        assertEquals("[[id, name, password]\n" +
                    "[0, user0, password0]\n" +
                    ", [id, name, password]\n" +
                    "[1, user1, password1]\n" +
                    ", [id, name, password]\n" +
                    "[2, user2, password2]\n" +
                    ", [id, name, password]\n" +
                    "[3, user3, password3]\n" +
                    ", [id, name, password]\n" +
                    "[4, user4, password4]\n" +
                    ", [id, name, password]\n" +
                    "[5, user5, password5]\n" +
                    ", [id, name, password]\n" +
                    "[6, user6, password6]\n" +
                    "]", data.toString());
    }

    private void addRows() {
        dbManager.connect("sqlcmd", "postgres", "postgres");
        dbManager.clearTable("test");
        DataSet rowToAdd = new DataSet();
        for (int i = 0; i < 7; i++) {
            rowToAdd.add("id", i);
            rowToAdd.add("name", "user" + i);
            rowToAdd.add("password", "password" + i);
            dbManager.insertRow("test", rowToAdd);
        }
    }

    @Test
    public void testGetTableDataWrongTableName() {
        dbManager.connect("sqlcmd", "postgres", "postgres");
        try  {
            List<DataSet> data = dbManager.getTableData("wrongtable");
        } catch (Exception e) {
            assertEquals("Ошибка sql query: SELECT * FROM wrongtable", e.getMessage());
        }
    }

    @Test
    public void testGetTableDataEmptyTable() {
        dbManager.connect("sqlcmd", "postgres", "postgres");
        dbManager.clearTable("test");
        List<DataSet> data = dbManager.getTableData("test");
        assertEquals("[]", data.toString());
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
        connectAndClearTableTest();

        DataSet rowToAdd = new DataSet();
        rowToAdd.add("id", 13);
        rowToAdd.add("name", "Stiven");
        rowToAdd.add("password", "pass");
        dbManager.insertRow("test", rowToAdd);
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
        Set<String> expectedTableNames = new TreeSet<>(tablesWithoutTableTest);
        expectedTableNames.add("test");
        assertEquals(expectedTableNames.toString(),dbManager.getTableNames().toString());
    }

    @Test
    public void testUpdateTableDataTwoCondition() {
        // given
        connectAndClearTableTest();

        // when
        DataSet input = new DataSet();
        input.add("id", 13);
        input.add("name", "Stiven");
        input.add("password", "pass");
        dbManager.insertRow("test", input);

        // then
        DataSet dataToUpdate = new DataSet();
        dataToUpdate.add("name", "StivenPupkin");
        dataToUpdate.add("password", "PupkinPassword");

        DataSet condition = new DataSet();
        condition.add("id", 13);
        condition.add("name", "Stiven");

        dbManager.updateQuery("test", dataToUpdate, condition);

        List<DataSet> data = dbManager.getTableData("test");
        DataSet user = data.get(0);
        assertEquals("[id, name, password]", user.getNames().toString());
        assertEquals("[13, StivenPupkin, PupkinPassword]", user.getValues().toString());
    }

    @Test
    public void testUpdateTableData() {
        // given
        connectAndClearTableTest();

        // when
        DataSet input = new DataSet();
        input.add("id", 13);
        input.add("name", "Stiven");
        input.add("password", "pass");
        dbManager.insertRow("test", input);

        // then
        DataSet dataToUpdate = new DataSet();
        dataToUpdate.add("name", "StivenPupkin");
        dataToUpdate.add("password", "PupkinPassword");

        DataSet condition = new DataSet();
        condition.add("id", 13);

        dbManager.updateQuery("test", dataToUpdate, condition);

        List<DataSet> data = dbManager.getTableData("test");
        DataSet user = data.get(0);
        assertEquals("[id, name, password]", user.getNames().toString());
        assertEquals("[13, StivenPupkin, PupkinPassword]", user.getValues().toString());
    }

    @Test
    public void testInsertTableData() {
        // given
        connectAndClearTableTest();

        // when
        DataSet input = new DataSet();
        input.add("id", 13);
        input.add("name", "Stiven");
        input.add("password", "pass");
        dbManager.insertRow("test", input);

        // then
        List<DataSet> data = dbManager.getTableData("test");
        assertEquals(1, data.size());

        DataSet user = data.get(0);
        assertEquals("[id, name, password]", user.getNames().toString());
        assertEquals("[13, Stiven, pass]", user.getValues().toString());

        for (int i = 1; i < 13; i++) {
            input = new DataSet();
            input.add("id", i);
            input.add("name", "user" + i);
            input.add("password", "pass" + i);
            dbManager.insertRow("test", input);
        }
        data = dbManager.getTableData("test");
        assertEquals(13, data.size());
    }

    private void connectAndClearTableTest() {
        dbManager.connect("sqlcmd", "postgres", "postgres");
        dbManager.execQuery("DELETE FROM test");
    }
}
