package ua.com.juja.vitvyaz.sqlcmd.integration;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.juja.vitvyaz.sqlcmd.Main;
import ua.com.juja.vitvyaz.sqlcmd.model.DatabaseManager;
import ua.com.juja.vitvyaz.sqlcmd.model.JDBCPosgreManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

/**
 * Created by Vitalii Viazovoi on 16.04.2016.
 */
public class IntegrationTest {

    private static ConfigurabeInputStream in;
    private static ByteArrayOutputStream out;

    @BeforeClass
    public static void init() {
        DatabaseManager dbManager = new JDBCPosgreManager();
        dbManager.connect("sqlcmd", "postgres", "postgres");
        dbManager.dropTable("test");
        dbManager.createTable("CREATE TABLE test (id int PRIMARY KEY NOT NULL, name text, password text)");
    }


    @Before
    public void setup() {
        in = new ConfigurabeInputStream();
        out = new ByteArrayOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
    }


    @Test
    public void ConnectAndExitTest() {
        //given
        in.add("sqlcmd");
        in.add("postgres");
        in.add("postgres");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Введите название базы данных(sqlcmd): \r\n" +
                //sqlcmd
                "Введите имя пользователя: \r\n" +
                //postgres
                "Введите пароль: \r\n" +
                //postgres
                "Подключение к базе данных выполнено\r\n" +
                "Введите команду:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    public String getData() {
        try {
            return new String(out.toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Test
    public void ConnectWrongUserAndExitTest() {
        //given
        in.add("sqlcmd");
        in.add("postgres");
        in.add("postgre");
        in.add("no");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Введите название базы данных(sqlcmd): \r\n" +
                //sqlcmd
                "Введите имя пользователя: \r\n" +
                //postgres
                "Введите пароль: \r\n" +
                //postgres
                "Не удается подключиться к базе данных: sqlcmd имя пользователя: postgres Ошибка при попытке подсоединения.\r\n" +
                "Повторить попытку? (yes/no):\r\n" +
                //no
                "До свидания!\r\n", getData());
    }

    @Test
    public void DropAndCreateTableTest() {
        //given
        in.add("sqlcmd");
        in.add("postgres");
        in.add("postgres");
        in.add("drop test");
        in.add("no");
        in.add("drop test");
        in.add("yes");
        in.add("create test (id int PRIMARY KEY NOT NULL, name text, password text)");
        in.add("find test");
        in.add("exit");


        //when
        Main.main(new String[0]);

        //then
        assertEquals("Введите название базы данных(sqlcmd): \r\n" +
                //sqlcmd
                "Введите имя пользователя: \r\n" +
                //postgres
                "Введите пароль: \r\n" +
                //postgres
                "Подключение к базе данных выполнено\r\n" +
                "Введите команду:\r\n" +
                //drop test
                "Удаляем таблицу 'test'! Для подтверждения введите 'yes':\r\n" +
                //no
                "Удаление таблицы отменено\r\n" +
                "Введите команду:\r\n" +
                //drop test
                "Удаляем таблицу 'test'! Для подтверждения введите 'yes':\r\n" +
                //yes
                "Таблица test удалена\r\n" +
                "Введите команду:\r\n" +
                //create test (id int PRIMARY KEY NOT NULL, name text, password text)
                "Таблица test создана\r\n" +
                "Введите команду:\r\n" +
                //find test
                "------------------------\r\n" +
                "| id | name | password |\r\n" +
                "------------------------\r\n" +
                "Введите команду:\r\n" +
                "До свидания!\r\n", getData());
    }


    @Test
    public void ClearTableTest() {
        //given
        in.add("sqlcmd");
        in.add("postgres");
        in.add("postgres");
        in.add("clear test");
        in.add("no");
        in.add("clear test");
        in.add("yes");
        in.add("exit");


        //when
        Main.main(new String[0]);

        //then
        assertEquals("Введите название базы данных(sqlcmd): \r\n" +
                //sqlcmd
                "Введите имя пользователя: \r\n" +
                //postgres
                "Введите пароль: \r\n" +
                //postgres
                "Подключение к базе данных выполнено\r\n" +
                "Введите команду:\r\n" +
                //clear test
                "Удаляем все строки таблицы 'test'! Для подтверждения введите 'yes':\r\n" +
                //no
                "Удаление строк таблицы отменено\r\n" +
                "Введите команду:\r\n" +
                //clear test
                "Удаляем все строки таблицы 'test'! Для подтверждения введите 'yes':\r\n" +
                //yes
                "Все строки в таблице test удалены\r\n" +
                "Введите команду:\r\n" +
                "До свидания!\r\n", getData());
    }

    @Test
    public void ClearTableWrongInputTest() {
        //given
        in.add("sqlcmd");
        in.add("postgres");
        in.add("postgres");
        in.add("clear");
        in.add("clear wrongtablename");
        in.add("exit");


        //when
        Main.main(new String[0]);

        //then
        assertEquals("Введите название базы данных(sqlcmd): \r\n" +
                //sqlcmd
                "Введите имя пользователя: \r\n" +
                //postgres
                "Введите пароль: \r\n" +
                //postgres
                "Подключение к базе данных выполнено\r\n" +
                "Введите команду:\r\n" +
                //clear
                "Ошибка! Введено неправильное количество параметров команды clear \r\n" +
                "Введите команду:\r\n" +
                //clear wrongtablename
                "Ошибка! Нет такой таблицы. Доступны таблицы:\n" +
                "[test, users]\r\n" +
                "Введите команду:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void DropTableWrongInputTest() {
        //given
        in.add("sqlcmd");
        in.add("postgres");
        in.add("postgres");
        in.add("drop");
        in.add("drop wrongtablename");
        in.add("exit");


        //when
        Main.main(new String[0]);

        //then
        assertEquals("Введите название базы данных(sqlcmd): \r\n" +
                //sqlcmd
                "Введите имя пользователя: \r\n" +
                //postgres
                "Введите пароль: \r\n" +
                //postgres
                "Подключение к базе данных выполнено\r\n" +
                "Введите команду:\r\n" +
                //drop
                "Ошибка! Введено неправильное количество параметров команды drop \r\n" +
                "Введите команду:\r\n" +
                //drop wrongtablename
                "Ошибка! Нет такой таблицы. Доступны таблицы:\n" +
                "[test, users]\r\n" +
                "Введите команду:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void InsertAndFindTest() {
        //given
        in.add("sqlcmd");
        in.add("postgres");
        in.add("postgres");
        in.add("clear test");
        in.add("yes");
        in.add("insert test");
        in.add("id 1 name Vasya password pass1");
        in.add("insert test");
        in.add("id 2 name Petya password pass2");
        in.add("find test");
        in.add("find test 2 1");
        in.add("exit");


        //when
        Main.main(new String[0]);

        //then
        assertEquals("Введите название базы данных(sqlcmd): \r\n" +
                //sqlcmd
                "Введите имя пользователя: \r\n" +
                //postgres
                "Введите пароль: \r\n" +
                //postgres
                "Подключение к базе данных выполнено\r\n" +
                "Введите команду:\r\n" +
                //clear test
                "Удаляем все строки таблицы 'test'! Для подтверждения введите 'yes':\r\n" +
                //yes
                "Все строки в таблице test удалены\r\n" +
                "Введите команду:\r\n" +
                //insert test
                "Введите данные в формате: field1 newValue1 field2 newValue2 ... \r\n" +
                //id 1 name Vasya password pass1
                "Строка добавлена\r\n" +
                "Введите команду:\r\n" +
                //insert test
                "Введите данные в формате: field1 newValue1 field2 newValue2 ... \r\n" +
                //id 2 name Petya password pass2
                "Строка добавлена\r\n" +
                "Введите команду:\r\n" +
                //find test
                "-------------------------\r\n" +
                "| id |  name | password |\r\n" +
                "-------------------------\r\n" +
                "|  1 | Vasya |    pass1 |\r\n" +
                "|  2 | Petya |    pass2 |\r\n" +
                "Введите команду:\r\n" +
                //find test 2 1
                "-------------------------\r\n" +
                "| id |  name | password |\r\n" +
                "-------------------------\r\n" +
                "|  2 | Petya |    pass2 |\r\n" +
                //exit
                "Введите команду:\r\n" +
                "До свидания!\r\n", getData());
    }

    @Test
    public void FindWrongInputTest() {
        //given
        in.add("sqlcmd");
        in.add("postgres");
        in.add("postgres");
        in.add("find");
        in.add("find wrongtablename");
        in.add("exit");


        //when
        Main.main(new String[0]);

        //then
        assertEquals("Введите название базы данных(sqlcmd): \r\n" +
                //sqlcmd
                "Введите имя пользователя: \r\n" +
                //postgres
                "Введите пароль: \r\n" +
                //postgres
                "Подключение к базе данных выполнено\r\n" +
                "Введите команду:\r\n" +
                //find
                "Ошибка! Введено неправильное количество параметров команды find \r\n" +
                "Введите команду:\r\n" +
                //find wrongtablename
                "Ошибка! Нет такой таблицы. Доступны таблицы:\n" +
                "[test, users]\r\n" +
                "Введите команду:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void HelpTest() {
        //given
        in.add("sqlcmd");
        in.add("postgres");
        in.add("postgres");
        in.add("help");
        in.add("exit");


        //when
        Main.main(new String[0]);

        //then

        assertEquals("Введите название базы данных(sqlcmd): \r\n" +
                //sqlcmd
                "Введите имя пользователя: \r\n" +
                //postgres
                "Введите пароль: \r\n" +
                //postgres
                "Подключение к базе данных выполнено\r\n" +
                "Введите команду:\r\n" +
                //help
                "Существующие команды:\r\n" +
                "\thelp\n" +
                "\t\tвывести список команд\r\n" +
                "\texit\n" +
                "\t\tвыход из программы\r\n" +
                "\tlist\n" +
                "\t\tвывести список таблиц\r\n" +
                "\tfind tableName [LIMIT OFFET]\n" +
                "\t\tвывести содержимое таблицы [LIMIT - количество строк OFFSET - начальная строка]\r\n" +
                "\tupdate tableName ID\n" +
                "\t\tизменить строку таблицы tableName (ID - идентификатор строки)\r\n" +
                "\tinsert tableName\n" +
                "\t\tвставить строку в таблицу\r\n" +
                "\tclear tableName\n" +
                "\t\tочистить содержимое таблицы\r\n" +
                "\tcreate tableName ( columnName1 dataType1 [PRIMARY KEY] [NOT NULL], ... " +
                "columnNameN dataTypeN [NOT NULL] )\n" +
                "\t\tсоздать таблицу\r\n" +
                "\tdrop tableName\n" +
                "\t\tудалить таблицу\r\n" +
                "Введите команду:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void InsertWrongInputTest() {
        //given
        in.add("sqlcmd");
        in.add("postgres");
        in.add("postgres");
        in.add("insert");
        in.add("insert wrongtablename");
        in.add("insert test");
        in.add("id 1 name");
        in.add("exit");


        //when
        Main.main(new String[0]);

        //then
        assertEquals("Введите название базы данных(sqlcmd): \r\n" +
                //sqlcmd
                "Введите имя пользователя: \r\n" +
                //postgres
                "Введите пароль: \r\n" +
                //postgres
                "Подключение к базе данных выполнено\r\n" +
                "Введите команду:\r\n" +
                //insert
                "Ошибка! Введено неправильное количество параметров команды insert \r\n" +
                "Введите команду:\r\n" +
                //insert wrongtablename
                "Ошибка! Нет такой таблицы. Доступны таблицы:\n" +
                "[test, users]\r\n" +
                "Введите команду:\r\n" +
                //insert test
                "Введите данные в формате: field1 newValue1 field2 newValue2 ... \r\n" +
                //id 1 name
                "Ошибка! Нечетное количество параметров\r\n" +
                "Введите команду:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void ListTest() {
        //given
        in.add("sqlcmd");
        in.add("postgres");
        in.add("postgres");
        in.add("list");
        in.add("exit");


        //when
        Main.main(new String[0]);

        //then
        assertEquals("Введите название базы данных(sqlcmd): \r\n" +
                //sqlcmd
                "Введите имя пользователя: \r\n" +
                //postgres
                "Введите пароль: \r\n" +
                //postgres
                "Подключение к базе данных выполнено\r\n" +
                "Введите команду:\r\n" +
                //list
                "[test, users]\r\n" +
                "Введите команду:\r\n" +
                "До свидания!\r\n", getData());
    }

    @Test
    public void UnsupportedTest() {
        //given
        in.add("sqlcmd");
        in.add("postgres");
        in.add("postgres");
        in.add("wrongcommand");
        in.add("exit");


        //when
        Main.main(new String[0]);

        //then
        assertEquals("Введите название базы данных(sqlcmd): \r\n" +
                //sqlcmd
                "Введите имя пользователя: \r\n" +
                //postgres
                "Введите пароль: \r\n" +
                //postgres
                "Подключение к базе данных выполнено\r\n" +
                "Введите команду:\r\n" +
                //wrongcommand
                "Введите правильно команду. (help - вывод списка команд)\r\n" +
                "Введите команду:\r\n" +
                "До свидания!\r\n", getData());
    }

    @Test
    public void UpdateTest() {
        //given
        in.add("sqlcmd");
        in.add("postgres");
        in.add("postgres");
        in.add("clear test");
        in.add("yes");
        in.add("insert test");
        in.add("id 1 name Vasya password pass1");
        in.add("insert test");
        in.add("id 2 name Petya password pass2");
        in.add("find test");
        in.add("update test 1");
        in.add("name VasyaPupkin");
        in.add("find test");
        in.add("exit");


        //when
        Main.main(new String[0]);

        //then
        assertEquals("Введите название базы данных(sqlcmd): \r\n" +
                //sqlcmd
                "Введите имя пользователя: \r\n" +
                //postgres
                "Введите пароль: \r\n" +
                //postgres
                "Подключение к базе данных выполнено\r\n" +
                "Введите команду:\r\n" +
                //clear test
                "Удаляем все строки таблицы 'test'! Для подтверждения введите 'yes':\r\n" +
                //yes
                "Все строки в таблице test удалены\r\n" +
                "Введите команду:\r\n" +
                //insert test
                "Введите данные в формате: field1 newValue1 field2 newValue2 ... \r\n" +
                //id 1 name Vasya password pass1
                "Строка добавлена\r\n" +
                "Введите команду:\r\n" +
                //insert test
                "Введите данные в формате: field1 newValue1 field2 newValue2 ... \r\n" +
                //id 2 name Petya password pass2
                "Строка добавлена\r\n" +
                "Введите команду:\r\n" +
                //find test
                "-------------------------\r\n" +
                "| id |  name | password |\r\n" +
                "-------------------------\r\n" +
                "|  1 | Vasya |    pass1 |\r\n" +
                "|  2 | Petya |    pass2 |\r\n" +
                "Введите команду:\r\n" +
                //update test 1
                "-------------------------\n" +
                "| id |  name | password |\n" +
                "-------------------------\n" +
                "|  1 | Vasya |    pass1 |\n" +
                "-------------------------\r\n" +
                "Введите данные к изменению в формате: field1 newValue1 field2 newValue2 ... \r\n" +
                //id 1 name VasyaPupkin
                "Измененная строка:\r\n" +
                "-------------------------------\n" +
                "| id |        name | password |\n" +
                "-------------------------------\n" +
                "|  1 | VasyaPupkin |    pass1 |\n" +
                "-------------------------------\r\n" +
                //find test
                "Введите команду:\r\n" +
                "-------------------------------\r\n" +
                "| id |        name | password |\r\n" +
                "-------------------------------\r\n" +
                "|  2 |       Petya |    pass2 |\r\n" +
                "|  1 | VasyaPupkin |    pass1 |\r\n" +
                //exit
                "Введите команду:\r\n" +
                "До свидания!\r\n", getData());
    }

    @Test
    public void UpdateWrongInputTest() {
        //given
        in.add("sqlcmd");
        in.add("postgres");
        in.add("postgres");
        in.add("update");
        in.add("update wrongtablename 1");
        in.add("update test 77");
        in.add("insert test");
        in.add("id 1 name Vasya password pass1");
        in.add("update test 1");
        in.add("name");
        in.add("exit");


        //when
        Main.main(new String[0]);

        //then
        assertEquals("Введите название базы данных(sqlcmd): \r\n" +
                //sqlcmd
                "Введите имя пользователя: \r\n" +
                //postgres
                "Введите пароль: \r\n" +
                //postgres
                "Подключение к базе данных выполнено\r\n" +
                "Введите команду:\r\n" +
                //update
                "Ошибка! Введено неправильное количество параметров команды update \r\n" +
                "Введите команду:\r\n" +
                //update wrongtablename 1
                "Ошибка! Нет такой таблицы. Доступны таблицы:\n" +
                "[test, users]\r\n" +
                "Введите команду:\r\n" +
                //update test 77
                "В таблице test нет строки с id: 77\r\n" +
                "Введите команду:\r\n" +
                //insert test
                "Введите данные в формате: field1 newValue1 field2 newValue2 ... \r\n" +
                //id 1 name Vasya password pass1
                "Строка добавлена\r\n" +
                "Введите команду:\r\n" +
                //update test 1
                "-------------------------\n" +
                "| id |  name | password |\n" +
                "-------------------------\n" +
                "|  1 | Vasya |    pass1 |\n" +
                "-------------------------\r\n" +
                "Введите данные к изменению в формате: field1 newValue1 field2 newValue2 ... \r\n" +
                //name
                "Ошибка! Нечетное количество параметров\r\n" +
                "Введите команду:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

}
