package ua.com.juja.vitvyaz.sqlcmd.model;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Vitalii Viazovoi on 24.02.2016.
 */
public class DataSet {

    private static class Data {
        String name;
        Object value;

        public Data(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }
    }

    private ArrayList<Data> datas = new ArrayList<>();


    public void add(String name, Object value) {
        datas.add(new Data(name, value));
    }

    @Override
    public String toString() {
        return getNames().toString() + "\n" + getValues().toString() +"\n";
    }

    public ArrayList<String> getNames() {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            result.add(datas.get(i).getName());
        }
        return result;
    }

    public String getNamesFormated(String format) {
        StringBuffer names = new StringBuffer();
        for(String i: getNames()) {
            names.append(String.format(format, i));
        }
        return names.substring(0, names.length() - 1);
    }

    public String getValuesFormated(String format) {
        StringBuffer values = new StringBuffer();
        for(Object i: getValues()) {
            values.append(String.format(format, i));
        }
        return values.substring(0, values.length() - 1);
    }

    public ArrayList<Object> getValues() {
        ArrayList<Object> result = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            result.add(datas.get(i).getValue());
        }
        return result;
    }

    public String getName (int i) {
        return datas.get(i).getName();
    }

    public Object getValue (int i) {
        return datas.get(i).getValue();
    }

    public int size() {
        return datas.size();
    }

    public String getTable() {
        if (datas.size() == 0) {
            return "";
        }
        StringBuffer names = new StringBuffer();
        StringBuffer values = new StringBuffer();
        for (int i = 0; i < datas.size(); i++) {
            int maxLength = Math.max(getName(i).length(), getValue(i).toString().length());
            String format = "| %" + maxLength + "s ";
            names.append(String.format(format, getName(i)));
            values.append(String.format(format, getValue(i)));
        }
        names.append("|");
        values.append("|");
        String lineString = repeatString("-", names.length());
        String result = lineString + "\n" +
                names.toString() + "\n"+
                lineString + "\n" +
                values.toString() + "\n" +
                lineString;
        return result;
    }

    private String repeatString(String s, int times) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < times; i++) {
            result.append(s);
        }
        return result.toString();
    }




}
