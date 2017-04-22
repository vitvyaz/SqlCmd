package ua.com.juja.vitvyaz.sqlcmd.web;

/**
 * @author Vitalii Viazovoi
 */
public class NullAction extends AbstractAction {

    @Override
    public boolean canProcess(String url) {
        return false;
    }
}
