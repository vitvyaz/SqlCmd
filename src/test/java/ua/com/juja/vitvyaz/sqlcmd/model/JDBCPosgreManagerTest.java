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

    private final static String DB_NAME = "sqlcmd";
    private final static String DB_USER = "postgres";
    private final static String DB_PASSWORD = "postgres";

    private static Set<String> tablesWithoutTableTest = new HashSet<>();
    private JDBCPostgreManager dbManager;

    @BeforeClass
    public static void init() {
        DatabaseManager dbManager = new JDBCPostgreManager();
        dbManager.connect(DB_NAME, DB_USER, DB_PASSWORD);
        dbManager.dropTable("test");
        tablesWithoutTableTest = dbManager.getTableNames();
        dbManager.createTable("test (id int PRIMARY KEY NOT NULL, name text, password text)");
    }

    @Before
    public void setup() {
        dbManager = new JDBCPostgreManager();
        dbManager.connect(DB_NAME, DB_USER, DB_PASSWORD);
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
        try  {
            List<DataSet> data = dbManager.getTableData("wrongtable");
        } catch (Exception e) {
            assertEquals("Ошибка sql query: SELECT * FROM wrongtable", e.getMessage());
        }
    }

    @Test
    public void testGetTableDataEmptyTable() {
        dbManager.clearTable("test");
        List<DataSet> data = dbManager.getTableData("test");
        assertEquals("[]", data.toString());
    }

    @Test
    public void testGetTableColumns() {
        Set<String> tableColumns = dbManager.getTableColumns("test");
        assertEquals("[id, name, password]", tableColumns.toString());
    }

    @Test
    public void testGetTableColumnsWrongTableName() {
        try {
            Set<String> tableColumns = dbManager.getTableColumns("wrongtable");
        } catch (IllegalArgumentException e) {
            assertEquals("Ошибка! Нет таблицы с именем: wrongtable", e.getMessage());
        }
    }

    @Test
    public void testClearTable() {
        dbManager.clearTable("test");

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
        try {
            dbManager.clearTable("wrongtable");
            fail();
        } catch (Exception e) {
            assertEquals("Ошибка sql query: DELETE FROM wrongtable", e.getMessage());
        }
    }

    @Test
    public void testDisconect() {
        dbManager.disconnect();
        assertFalse(dbManager.isConnected());
    }

    @Test
    public void testIsTableExist() {
        assertTrue(dbManager.existTable("test"));
    }

    @Test
    public void testIsTableExistWrongTableName() {
        assertFalse(dbManager.existTable("wrongtable"));
    }

    @Test
    public void testIsTableExistEmptyTableName() {
        assertFalse(dbManager.existTable(""));
    }

    @Test
    public void testConnectWrongPassword() {
        dbManager.disconnect();
        try {
            dbManager.connect(DB_NAME, DB_USER, "wrongPassword");
            fail();
        } catch (Exception e) {
            assertEquals("Не удается подключиться к базе данных: sqlcmd имя пользователя: postgres", e.getMessage());
        }
    }

    @Test
    public void testConnectWrongUser() {
        dbManager.disconnect();
        try {
            dbManager.connect(DB_NAME, "wronguser", DB_PASSWORD);
            fail();
        } catch (RuntimeException e) {
            assertEquals("Не удается подключиться к базе данных: sqlcmd имя пользователя: wronguser", e.getMessage());
        }
    }

    @Test
    public void testConnectWrongDatabase() {
        dbManager.disconnect();
        try {
            dbManager.connect("wrongdatabase", DB_USER, DB_PASSWORD);
            fail();
        } catch (RuntimeException e) {
            assertEquals("Не удается подключиться к базе данных: wrongdatabase имя пользователя: postgres", e.getMessage());
        }
    }

    @Test
    public void testIsConnectedWhenConnect() {
        assertTrue(dbManager.isConnected());
    }

    @Test
    public void testIsConnectedWhenNotConnect() {
        dbManager.disconnect();
        assertFalse(dbManager.isConnected());
    }


    @Test
    public void testGetAllTableNames() {
        Set<String> expectedTableNames = new HashSet<>(tablesWithoutTableTest);
        expectedTableNames.add("test");
        assertEquals(expectedTableNames.toString(),dbManager.getTableNames().toString());
    }

    @Test
    public void testUpdateTableDataTwoCondition() {
        // given
        dbManager.clearTable("test");

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

        dbManager.update("test", dataToUpdate, condition);

        List<DataSet> data = dbManager.getTableData("test");
        DataSet user = data.get(0);
        assertEquals("[id, name, password]", user.getNames().toString());
        assertEquals("[13, StivenPupkin, PupkinPassword]", user.getValues().toString());
    }

    @Test
    public void testUpdateTableData() {
        // given
        dbManager.clearTable("test");

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

        dbManager.update("test", dataToUpdate, condition);

        List<DataSet> data = dbManager.getTableData("test");
        DataSet user = data.get(0);
        assertEquals("[id, name, password]", user.getNames().toString());
        assertEquals("[13, StivenPupkin, PupkinPassword]", user.getValues().toString());
    }

    @Test
    public void testInsertTableData() {
        // given
        dbManager.clearTable("test");

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

}
