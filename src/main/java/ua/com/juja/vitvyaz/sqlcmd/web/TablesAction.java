package ua.com.juja.vitvyaz.sqlcmd.web;

import ua.com.juja.vitvyaz.sqlcmd.service.Service;
import ua.com.juja.vitvyaz.sqlcmd.service.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Vitalii Viazovoi
 */
public class TablesAction extends AbstractAction {

    public TablesAction(Service service) {
        super(service);
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/tables");
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("tables", service.tables(getManager(req, resp)));
            req.getRequestDispatcher("tables.jsp").forward(req, resp);
        } catch (ServiceException e) {
            error(req, resp, e);
        }
    }
}
