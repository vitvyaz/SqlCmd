package ua.com.juja.vitvyaz.sqlcmd.model;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Vitalii Viazovoi on 18.06.2016.
 */
public class TableBuilderTest {

    private List<DataSet> data;

    @Test
    public void testTableBuilderWithTwoParameters() {
        TableBuilder table = initTableBuilder();
        assertEquals("--------------------------\n" +
                    "| id |   name | password |\n" +
                    "--------------------------\n" +
                    "|  1 | Pupkin |    ***** |\n" +
                    "|  2 | Petrov |   ###### |\n" +
                    "--------------------------", table.toString());
    }

    private TableBuilder initTableBuilder() {
        DataSet row1 = new DataSet();
        row1.add("id", 1);
        row1.add("name", "Pupkin");
        row1.add("password", "*****");
        DataSet row2 = new DataSet();
        row2.add("id", 2);
        row2.add("name", "Petrov");
        row2.add("password", "######");
        List<DataSet> data = new LinkedList<>();
        data.add(row1);
        data.add(row2);

        return new TableBuilder(data.get(0).getNames(), data);
    }


    @Test
    public void testTableBuilderWithOneParameter() {
        DataSet dataSet = new DataSet();
        dataSet.add("id", 1);
        dataSet.add("name", "Pupkin");
        dataSet.add("password", "*****");

        TableBuilder table = new TableBuilder(dataSet);
        assertEquals("--------------------------\n" +
                "| id |   name | password |\n" +
                "--------------------------\n" +
                "|  1 | Pupkin |    ***** |\n" +
                "--------------------------", table.toString());
    }
}
