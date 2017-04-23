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
public class FindAction extends AbstractAction {

    public FindAction(Service service) {
        super(service);
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/find");
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tableName = req.getParameter("table");
        try {
            req.setAttribute("data", service.find(getManager(req, resp), tableName));
            req.getRequestDispatcher("find.jsp").forward(req, resp);
        } catch (ServiceException e) {
            error(req, resp, e);
        }
    }
}
