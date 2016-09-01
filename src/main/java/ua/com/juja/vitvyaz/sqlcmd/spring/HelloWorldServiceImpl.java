package ua.com.juja.vitvyaz.sqlcmd.spring;

/**
 * Created by Vitalii Viazovoi on 01.09.2016.
 */
public class HelloWorldServiceImpl implements Service {
    @Override
    public String getData() {
        return "Hello World";
    }
}
