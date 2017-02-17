package ua.com.juja.vitvyaz.sqlcmd.controller.web;

import ua.com.juja.vitvyaz.sqlcmd.model.DataSet;
import ua.com.juja.vitvyaz.sqlcmd.model.DatabaseManager;
import ua.com.juja.vitvyaz.sqlcmd.service.Service;
import ua.com.juja.vitvyaz.sqlcmd.service.ServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Виталий on 24.08.2016.
 */
public class MainServlet extends HttpServlet {
    private Service service;

    @Override
    public void init() throws ServletException {
        super.init();

        service = new ServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);

        if (action.startsWith("/connect")) {
            req.getRequestDispatcher("connect.jsp").forward(req, resp);
            return;
        }

        DatabaseManager dbManager = (DatabaseManager) req.getSession().getAttribute("db_manager");

        if (dbManager == null) {
            resp.sendRedirect(resp.encodeRedirectURL("connect"));
            return;
        }

        if (action.equals("/menu") || action.equals("/")) {
            req.setAttribute("items", service.commandsList());
            req.getRequestDispatcher("menu.jsp").forward(req, resp);

        } else if (action.startsWith("/help")) {
            req.getRequestDispatcher("help.jsp").forward(req, resp);

        } else if (action.startsWith("/find")) {
            String tableName = req.getParameter("table");
            req.setAttribute("data", service.find(dbManager, tableName));
            req.getRequestDispatcher("find.jsp").forward(req, resp);

        } else if (action.startsWith("/tables")) {
            req.setAttribute("tables", service.tables(dbManager));
            req.getRequestDispatcher("tables.jsp").forward(req, resp);

        } else if (action.startsWith("/create")) {
            req.getRequestDispatcher("create.jsp").forward(req, resp);

        } else if (action.startsWith("/insert")) {
            String tableName = req.getParameter("table");
            req.getSession().setAttribute("table", tableName);
            req.setAttribute("columns", service.columns(dbManager, tableName));
            req.getRequestDispatcher("insert.jsp").forward(req, resp);

        } else if (action.startsWith("/update")) {
            String tableName = req.getParameter("table");
            req.getSession().setAttribute("table", tableName);
            req.setAttribute("columns", service.columns(dbManager, tableName));
            req.getRequestDispatcher("update.jsp").forward(req, resp);

        } else if (action.startsWith("/clear")) {
            String tableName = req.getParameter("table");
            req.getSession().setAttribute("table", tableName);
            req.getRequestDispatcher("clear.jsp").forward(req, resp);

        } else if (action.startsWith("/drop")) {
            String tableName = req.getParameter("table");
            req.getSession().setAttribute("table", tableName);
            req.getRequestDispatcher("drop.jsp").forward(req, resp);

        } else {
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);

        if (action.equals("/connect")) {
            connect(req, resp);

        } else if (action.equals("/create")) {
            create(req, resp);

        } else if (action.equals("/insert")) {
            insert(req, resp);

        } else if (action.equals("/update")) {
            update(req, resp);

        } else if (action.equals("/clear")) {
            clear(req, resp);

        } else if (action.equals("/drop")) {
            drop(req, resp);
        }
    }

    private void drop(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        DatabaseManager dbManager = (DatabaseManager) req.getSession().getAttribute("db_manager");
        String tableName = (String) req.getSession().getAttribute("table");
        if (req.getParameter("confirm").equals("yes")) {
            try {
                dbManager.dropTable(tableName);
            } catch (Exception e) {
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }
            resp.sendRedirect(resp.encodeRedirectURL("tables"));
        } else {
            resp.sendRedirect(resp.encodeRedirectURL("find?table=" + tableName));
        }
    }

    private void clear(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        DatabaseManager dbManager = (DatabaseManager) req.getSession().getAttribute("db_manager");
        String tableName = (String) req.getSession().getAttribute("table");
        if (req.getParameter("confirm").equals("yes")) {
            try {
                dbManager.clearTable(tableName);
            } catch (Exception e) {
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }
        }
        resp.sendRedirect(resp.encodeRedirectURL("find?table=" + tableName));
    }

    private void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DatabaseManager dbManager = (DatabaseManager) req.getSession().getAttribute("db_manager");
        String tableName = (String) req.getSession().getAttribute("table");
        List<String> columns = service.columns(dbManager, tableName);
        DataSet dataToChange = new DataSet();
        for (String column : columns) {
            String value = req.getParameter(column + "value");
            dataToChange.add(column, value);
        }
        DataSet condition = new DataSet();
        condition.add(req.getParameter("condition_column"), req.getParameter("condition_value"));
        try {
            dbManager.update(tableName, dataToChange, condition);
            resp.sendRedirect(resp.encodeRedirectURL("find?table=" + tableName));
        } catch (Exception e) {
            req.setAttribute("message", e.getMessage());
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }

    }

    private void insert(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DatabaseManager dbManager = (DatabaseManager) req.getSession().getAttribute("db_manager");
        String tableName = (String) req.getSession().getAttribute("table");
        List<String> columns = service.columns(dbManager, tableName);
        DataSet dataToInsert = new DataSet();
        for (String column : columns) {
            String value = req.getParameter(column + "value");
            dataToInsert.add(column, value);
        }
        try {
            dbManager.insertRow(tableName, dataToInsert);
            resp.sendRedirect(resp.encodeRedirectURL("find?table=" + tableName));
        } catch (Exception e) {
            req.setAttribute("message", e.getMessage());
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }

    private void create(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DatabaseManager dbManager = (DatabaseManager) req.getSession().getAttribute("db_manager");

        try {
            String query = req.getParameter("query");
            dbManager.createTable(query);
            resp.sendRedirect(resp.encodeRedirectURL("menu"));
        } catch (Exception e) {
            req.setAttribute("message", e.getMessage());
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }

    private void connect(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String databaseName = req.getParameter("dbname");
        String userName = req.getParameter("username");
        String password = req.getParameter("password");
        try {
            DatabaseManager dbManager = service.connect(databaseName, userName, password);
            req.getSession().setAttribute("db_manager", dbManager);
            resp.sendRedirect(resp.encodeRedirectURL("menu"));
        } catch (Exception e) {
            req.setAttribute("message", e.getMessage());
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }

    private String getAction(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        return requestURI.substring(req.getContextPath().length(), requestURI.length());
    }
}
