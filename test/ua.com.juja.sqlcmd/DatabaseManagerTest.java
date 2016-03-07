package ua.com.juja.sqlcmd;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Vitalii Viazovoi on 01.03.2016.
 */
public class DatabaseManagerTest {

    private DatabaseManager dbManager;

    @Before
    public void setup() {
        dbManager = new DatabaseManager();
        dbManager.connect("sqlcmd", "postgres", "postgres");
    }


    @Test
    public void testGetAllTableNames() {
        assertEquals("[users, test]",dbManager.getTableNames().toString());
    }
}
