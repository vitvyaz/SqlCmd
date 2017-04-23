package ua.com.juja.vitvyaz.sqlcmd.web;

import ua.com.juja.vitvyaz.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Vitalii Viazovoi
 */
public class DropAction extends AbstractAction {

    public DropAction(Service service) {
        super(service);
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/drop");
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tableName = req.getParameter("table");
        req.getSession().setAttribute("table", tableName);
        req.getRequestDispatcher("drop.jsp").forward(req, resp);
    }

    @Override
    public void post(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tableName = (String) req.getSession().getAttribute("table");
        if (req.getParameter("confirm").equals("yes")) {
            try {
                getManager(req, resp).dropTable(tableName);
            } catch (Exception e) {
                error(req, resp, e);
            }
            resp.sendRedirect(resp.encodeRedirectURL("tables"));
        } else {
            resp.sendRedirect(resp.encodeRedirectURL("find?table=" + tableName));
        }
    }
}
