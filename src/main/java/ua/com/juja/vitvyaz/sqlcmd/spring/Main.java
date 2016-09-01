package ua.com.juja.vitvyaz.sqlcmd.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Vitalii Viazovoi on 01.09.2016.
 */
public class Main {
    public static void main(String []args){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"application-context.xml"});
        LabRat rat = (LabRat) context.getBean("rat");
        rat.sayHi();
        System.out.println(rat.getName());
    }
}
