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
public class ConnectAction extends AbstractAction {

    public ConnectAction(Service service) {
        super(service);
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/connect");
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("connect.jsp").forward(req, resp);
    }

    @Override
    public void post(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String databaseName = req.getParameter("dbname");
        String userName = req.getParameter("username");
        String password = req.getParameter("password");
        try {
            DatabaseManager dbManager = service.connect(databaseName, userName, password);
            req.getSession().setAttribute("db_manager", dbManager);
            resp.sendRedirect(resp.encodeRedirectURL("menu"));
        } catch (Exception e) {
            error(req, resp, e);
        }
    }
}
