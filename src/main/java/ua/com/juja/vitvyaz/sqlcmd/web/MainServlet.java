package ua.com.juja.vitvyaz.sqlcmd.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ua.com.juja.vitvyaz.sqlcmd.service.Service;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Виталий on 24.08.2016.
 */
public class MainServlet extends HttpServlet {

    @Autowired
    private Service service;
    private List<Action> actions;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        actions = new LinkedList<>();
        actions.addAll(Arrays.asList(
                new ConnectAction(service),
                new MenuAction(service),
                new HelpAction(service),
                new FindAction(service),
                new TablesAction(service),
                new CreateAction(service),
                new InsertAction(service),
                new UpdateAction(service),
                new ClearAction(service),
                new DropAction(service),
                new ErrorAction(service)
        ));

        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Action action = getAction(req);
        action.get(req, resp);

//        String action = getActionName(req);
//
//        if (action.startsWith("/connect")) {
//            req.getRequestDispatcher("connect.jsp").forward(req, resp);
//            return;
//        }
//
//        DatabaseManager dbManager = (DatabaseManager) req.getSession().getAttribute("db_manager");
//
//        if (dbManager == null) {
//            resp.sendRedirect(resp.encodeRedirectURL("connect"));
//            return;
//        }
//
//        if (action.equals("/menu") || action.equals("/")) {
//            req.setAttribute("items", service.commandsList());
//            req.getRequestDispatcher("menu.jsp").forward(req, resp);
//
//        } else if (action.startsWith("/help")) {
//            req.getRequestDispatcher("help.jsp").forward(req, resp);
//
//        } else if (action.startsWith("/find")) {
//            String tableName = req.getParameter("table");
//            try {
//                req.setAttribute("data", service.find(dbManager, tableName));
//                req.getRequestDispatcher("find.jsp").forward(req, resp);
//            } catch (ServiceException e) {
//                error(req, resp, e);
//            }
//
//        } else if (action.startsWith("/tables")) {
//            try {
//                req.setAttribute("tables", service.tables(dbManager));
//                req.getRequestDispatcher("tables.jsp").forward(req, resp);
//            } catch (ServiceException e) {
//                error(req, resp, e);
//            }
//
//        } else if (action.startsWith("/create")) {
//            req.getRequestDispatcher("create.jsp").forward(req, resp);
//
//        } else if (action.startsWith("/insert")) {
//            try {
//                String tableName = req.getParameter("table");
//                req.getSession().setAttribute("table", tableName);
//                req.setAttribute("columns", service.columns(dbManager, tableName));
//                req.getRequestDispatcher("insert.jsp").forward(req, resp);
//            } catch (ServiceException e) {
//                error(req, resp, e);
//            }
//
//        } else if (action.startsWith("/update")) {
//            try {
//                String tableName = req.getParameter("table");
//                req.getSession().setAttribute("table", tableName);
//                req.setAttribute("columns", service.columns(dbManager, tableName));
//                req.getRequestDispatcher("update.jsp").forward(req, resp);
//            } catch (ServiceException e) {
//                error(req, resp, e);
//            }
//
//        } else if (action.startsWith("/clear")) {
//            String tableName = req.getParameter("table");
//            req.getSession().setAttribute("table", tableName);
//            req.getRequestDispatcher("clear.jsp").forward(req, resp);
//
//        } else if (action.startsWith("/drop")) {
//            String tableName = req.getParameter("table");
//            req.getSession().setAttribute("table", tableName);
//            req.getRequestDispatcher("drop.jsp").forward(req, resp);
//
//        } else {
//            req.getRequestDispatcher("error.jsp").forward(req, resp);
//        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String action = getActionName(req);

        Action action = getAction(req);
        action.post(req, resp);


//        if (action.equals("/connect")) {
//            connect(req, resp);
//
//        } else if (action.equals("/create")) {
//            create(req, resp);
//
//        } else if (action.equals("/insert")) {
//            insert(req, resp);
//
//        } else if (action.equals("/update")) {
//            update(req, resp);
//
//        } else if (action.equals("/clear")) {
//            clear(req, resp);
//
//        } else if (action.equals("/drop")) {
//            drop(req, resp);
//        }
    }

//    private void drop(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
//        DatabaseManager dbManager = (DatabaseManager) req.getSession().getAttribute("db_manager");
//        String tableName = (String) req.getSession().getAttribute("table");
//        if (req.getParameter("confirm").equals("yes")) {
//            try {
//                dbManager.dropTable(tableName);
//            } catch (Exception e) {
//                error(req, resp, e);
//            }
//            resp.sendRedirect(resp.encodeRedirectURL("tables"));
//        } else {
//            resp.sendRedirect(resp.encodeRedirectURL("find?table=" + tableName));
//        }
//    }

//    private void clear(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
//        DatabaseManager dbManager = (DatabaseManager) req.getSession().getAttribute("db_manager");
//        String tableName = (String) req.getSession().getAttribute("table");
//        if (req.getParameter("confirm").equals("yes")) {
//            try {
//                dbManager.clearTable(tableName);
//            } catch (Exception e) {
//                error(req, resp, e);
//            }
//        }
//        resp.sendRedirect(resp.encodeRedirectURL("find?table=" + tableName));
//    }

//    private void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        DatabaseManager dbManager = (DatabaseManager) req.getSession().getAttribute("db_manager");
//        String tableName = (String) req.getSession().getAttribute("table");
//        try {
//            List<String> columns = service.columns(dbManager, tableName);
//            DataSet dataToChange = new DataSet();
//            for (String column : columns) {
//                String value = req.getParameter(column + "value");
//                dataToChange.add(column, value);
//            }
//            DataSet condition = new DataSet();
//            condition.add(req.getParameter("condition_column"), req.getParameter("condition_value"));
//            dbManager.update(tableName, dataToChange, condition);
//            resp.sendRedirect(resp.encodeRedirectURL("find?table=" + tableName));
//        } catch (ServiceException e) {
//            error(req, resp, e);
//        }
//    }

//    private void insert(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        DatabaseManager dbManager = (DatabaseManager) req.getSession().getAttribute("db_manager");
//        String tableName = (String) req.getSession().getAttribute("table");
//        try {
//            List<String> columns = service.columns(dbManager, tableName);
//            DataSet dataToInsert = new DataSet();
//            for (String column : columns) {
//                String value = req.getParameter(column + "value");
//                dataToInsert.add(column, value);
//            }
//            dbManager.insertRow(tableName, dataToInsert);
//            resp.sendRedirect(resp.encodeRedirectURL("find?table=" + tableName));
//        } catch (Exception e) {
//            error(req, resp, e);
//        }
//    }

//    private void create(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        DatabaseManager dbManager = (DatabaseManager) req.getSession().getAttribute("db_manager");
//
//        try {
//            String query = req.getParameter("query");
//            dbManager.createTable(query);
//            resp.sendRedirect(resp.encodeRedirectURL("menu"));
//        } catch (Exception e) {
//            error(req, resp, e);
//        }
//    }

//    private void connect(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String databaseName = req.getParameter("dbname");
//        String userName = req.getParameter("username");
//        String password = req.getParameter("password");
//        try {
//            DatabaseManager dbManager = service.connect(databaseName, userName, password);
//            req.getSession().setAttribute("db_manager", dbManager);
//            resp.sendRedirect(resp.encodeRedirectURL("menu"));
//        } catch (Exception e) {
//            error(req, resp, e);
//        }
//    }

//    private void error(HttpServletRequest req, HttpServletResponse resp, Exception e) throws ServletException, IOException {
//        req.setAttribute("message", e.getMessage());
//        req.getRequestDispatcher("error.jsp").forward(req, resp);
//    }


//    private void error(HttpServletRequest req, HttpServletResponse resp, ServiceException e) {
//
//    }

    private String getActionName(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        return requestURI.substring(req.getContextPath().length(), requestURI.length());
    }

    private Action getAction(HttpServletRequest req) {
        String url = getActionName(req);
        for(Action action : actions) {
            if(action.canProcess(url)) {
                return action;
            }
        }
        return new NullAction(service);
    }
}
