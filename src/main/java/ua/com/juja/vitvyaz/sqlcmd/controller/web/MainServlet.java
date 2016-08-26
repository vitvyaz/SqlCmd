package ua.com.juja.vitvyaz.sqlcmd.controller.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Виталий on 24.08.2016.
 */
public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        String action = requestURI.substring(req.getContextPath().length(), requestURI.length());

        if (action.equals("/menu") || action.equals("/")) {
            req.getRequestDispatcher("menu.jsp").forward(req, resp);
        } else if (action.startsWith("/help")) {
            req.getRequestDispatcher("help.jsp").forward(req, resp);
//            resp.sendRedirect("help.jsp");
        } else {
            req.getRequestDispatcher("error.jsp").forward(req, resp);
//            resp.sendRedirect(resp.encodeRedirectURL("menu"));
//            resp.encodeRedirectURL(resp)
        }
    }
}
