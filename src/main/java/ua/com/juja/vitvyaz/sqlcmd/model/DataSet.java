package ua.com.juja.vitvyaz.sqlcmd.model;

import java.util.*;

/**
 * Created by Vitalii Viazovoi on 24.02.2016.
 */
public class DataSet {

    private Map<String, Object> data = new LinkedHashMap<>();

    public void add(String name, Object value) {
        data.put(name, value);
    }

    @Override
    public String toString() {
        return getNames().toString() + "\n" + getValues().toString() +"\n";
    }

    public Set<String> getNames() {
        return data.keySet();
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

    public List<Object> getValues() {
        return new ArrayList<Object>(data.values());
    }

    public Object getValue(String name) {
        return data.get(name);
    }

    public int size() {
        return data.size();
    }
}
