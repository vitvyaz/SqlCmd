package ua.com.juja.vitvyaz.sqlcmd.service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Виталий on 27.08.2016.
 */
public class ServiceImpl implements Service {
    @Override
    public List<String> commandsList() {
        return Arrays.asList("help", "connect", "menu");
    }
}
