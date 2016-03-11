package ua.com.juja.sqlcmd.model;

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




}
