package ua.com.juja.sqlcmd.model;

import org.junit.Test;
import ua.com.juja.sqlcmd.model.DataSet;

import static org.junit.Assert.assertEquals;

/**
 * Created by Vitalii Viazovoi on 02.03.2016.
 */
public class DataSetTest {

    @Test
    public void testAddData () {
        DataSet dataSet = new DataSet();
        dataSet.add("name", "Pupkin");
        dataSet.add("password", "pass");

        assertEquals("[name, password]", dataSet.getNames().toString());
        assertEquals("[Pupkin, pass]", dataSet.getValues().toString());
        assertEquals("[name, password]\n[Pupkin, pass]\n", dataSet.toString());
        assertEquals("name|password", dataSet.getNamesFormated("%s|"));

    }
}
