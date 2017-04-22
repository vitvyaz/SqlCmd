package ua.com.juja.vitvyaz.sqlcmd.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Vitalii Viazovoi
 */
public abstract class AbstractAction implements Action {

    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Do nothing
    }

    public void post(HttpServletRequest req, HttpServletResponse resp) {
        // Do nothing
    }
}
