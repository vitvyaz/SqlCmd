package ua.com.juja.vitvyaz.sqlcmd.web;

import ua.com.juja.vitvyaz.sqlcmd.service.Service;

/**
 * @author Vitalii Viazovoi
 */
public class NullAction extends AbstractAction {

    public NullAction(Service service) {
        super(service);
    }

    @Override
    public boolean canProcess(String url) {
        return false;
    }
}
