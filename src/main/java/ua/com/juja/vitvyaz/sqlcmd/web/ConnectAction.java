package ua.com.juja.vitvyaz.sqlcmd.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Vitalii Viazovoi
 */
public class ConnectAction extends AbstractAction {

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/connect");
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("connect.jsp").forward(req, resp);
    }
}
