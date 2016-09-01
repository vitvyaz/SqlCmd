package ua.com.juja.vitvyaz.sqlcmd.spring;

/**
 * Created by Vitalii Viazovoi on 01.09.2016.
 */
public class LabRat {

    private Service servise;
    private String text;
    private String name;

    public LabRat(String text) {
        this.text = text;
    }

    public String getText() {
        return text + "{" + servise.getData() + "}";
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name + "{" + servise.getData() + "}";
    }

    public void setServise(Service servise) {
        this.servise = servise;
    }

    public void sayHi(){
        System.out.println(text);
    }
}
