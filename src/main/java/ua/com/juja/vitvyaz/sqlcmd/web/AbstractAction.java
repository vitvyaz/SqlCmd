package ua.com.juja.vitvyaz.sqlcmd.web;

import ua.com.juja.vitvyaz.sqlcmd.model.DatabaseManager;
import ua.com.juja.vitvyaz.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Vitalii Viazovoi
 */
public abstract class AbstractAction implements Action {

    protected Service service;

    public AbstractAction(Service service) {
        this.service = service;
    }

    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Do nothing
    }

    public void post(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Do nothing
    }

    protected DatabaseManager getManager(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DatabaseManager dbManager = (DatabaseManager) req.getSession().getAttribute("db_manager");

        if (dbManager == null) {
            resp.sendRedirect(resp.encodeRedirectURL("connect"));
        }
        return dbManager;
    }

    protected void error(HttpServletRequest req, HttpServletResponse resp, Exception e) throws ServletException, IOException {
        req.setAttribute("message", e.getMessage());
        req.getRequestDispatcher("error.jsp").forward(req, resp);
    }
}
