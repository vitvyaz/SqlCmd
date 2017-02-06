package ua.com.juja.vitvyaz.sqlcmd.controller.web;

import ua.com.juja.vitvyaz.sqlcmd.model.DatabaseManager;
import ua.com.juja.vitvyaz.sqlcmd.service.Service;
import ua.com.juja.vitvyaz.sqlcmd.service.ServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
            req.setAttribute("columns", service.columns(dbManager, tableName));
            req.getRequestDispatcher("insert.jsp").forward(req, resp);

        } else {
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);

        if (action.equals("/connect")) {
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

        } else if (action.equals("/create")) {
            DatabaseManager dbManager = (DatabaseManager) req.getSession().getAttribute("db_manager");

            try {
                String query = req.getParameter("query");
                dbManager.createTable(query);
                req.getSession().setAttribute("db_manager", dbManager);
                resp.sendRedirect(resp.encodeRedirectURL("menu"));
            } catch (Exception e) {
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }
        }
    }

    private String getAction(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        return requestURI.substring(req.getContextPath().length(), requestURI.length());
    }
}
