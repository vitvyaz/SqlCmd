package ua.com.juja.vitvyaz.sqlcmd.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Vitalii Viazovoi
 */
public interface Action {

    boolean canProcess(String url);

    void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
    void post(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
}
