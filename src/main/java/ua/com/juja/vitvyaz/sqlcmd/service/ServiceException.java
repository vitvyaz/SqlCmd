package ua.com.juja.vitvyaz.sqlcmd.service;

/**
 * Created by methype on 29.03.2017.
 */
public class ServiceException extends Exception {
    public ServiceException(String message, Exception e) {
        super(message, e);
    }
}
