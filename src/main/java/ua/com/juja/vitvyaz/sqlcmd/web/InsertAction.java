package ua.com.juja.vitvyaz.sqlcmd.web;

import ua.com.juja.vitvyaz.sqlcmd.model.DataSet;
import ua.com.juja.vitvyaz.sqlcmd.model.DatabaseManager;
import ua.com.juja.vitvyaz.sqlcmd.service.Service;
import ua.com.juja.vitvyaz.sqlcmd.service.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Vitalii Viazovoi
 */
public class InsertAction extends AbstractAction {

    public InsertAction(Service service) {
        super(service);
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/insert");
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String tableName = req.getParameter("table");
            req.getSession().setAttribute("table", tableName);
            req.setAttribute("columns", service.columns(getManager(req, resp), tableName));
            req.getRequestDispatcher("insert.jsp").forward(req, resp);
        } catch (ServiceException e) {
            error(req, resp, e);
        }
    }

    @Override
    public void post(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DatabaseManager dbManager = getManager(req, resp);
        String tableName = (String) req.getSession().getAttribute("table");
        try {
            List<String> columns = service.columns(dbManager, tableName);
            DataSet dataToInsert = new DataSet();
            for (String column : columns) {
                String value = req.getParameter(column + "value");
                dataToInsert.add(column, value);
            }
            dbManager.insertRow(tableName, dataToInsert);
            resp.sendRedirect(resp.encodeRedirectURL("find?table=" + tableName));
        } catch (Exception e) {
            error(req, resp, e);
        }
    }
}
