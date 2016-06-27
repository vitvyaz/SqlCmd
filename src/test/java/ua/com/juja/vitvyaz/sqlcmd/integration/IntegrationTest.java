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
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;

/**
 * Created by Vitalii Viazovoi on 16.04.2016.
 */
public class IntegrationTest {

    private static ConfigurabeInputStream in;
    private static ByteArrayOutputStream out;
    private static Set<String> tablesWithoutTableTest = new HashSet<>();

    @BeforeClass
    public static void init() {
        DatabaseManager dbManager = new JDBCPosgreManager();
        dbManager.connect("sqlcmd", "postgres", "postgres");
        dbManager.dropTable("test");
        tablesWithoutTableTest = dbManager.getTableNames();
        dbManager.createTable("test (id int PRIMARY KEY NOT NULL, name text, password text)");
    }

    @Before
    public void setup() {
        in = new ConfigurabeInputStream();
        out = new ByteArrayOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @Test
    public void testConnectAndExit() {
        //given
        connectToDB();
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Введите название базы данных(sqlcmd): \n" +
                //sqlcmd
                "Введите имя пользователя: \n" +
                //postgres
                "Введите пароль: \n" +
                //postgres
                "Подключение к базе данных выполнено\n" +
                "Введите команду:\n" +
                //exit
                "До свидания!\n", getData());
    }

    public String getData() {
        try {
            return new String(out.toByteArray(), "UTF-8").replaceAll("\r\n", "\n");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Test
    public void testConnectWrongUserPasswordAndExit() {
        //given
        in.add("sqlcmd");
        in.add("postgres");
        in.add("postgre");
        in.add("no");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Введите название базы данных(sqlcmd): \n" +
                //sqlcmd
                "Введите имя пользователя: \n" +
                //postgres
                "Введите пароль: \n" +
                //postgres
                "Не удается подключиться к базе данных: sqlcmd имя пользователя: postgres" +
                        " Ошибка при попытке подсоединения.\n" +
                "Повторить попытку? (yes/no):\n" +
                //no
                "До свидания!\n", getData());
    }

    @Test
    public void testDropAndCreateTable() {
        //given
        connectToDB();
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
        assertEquals("Введите название базы данных(sqlcmd): \n" +
                //sqlcmd
                "Введите имя пользователя: \n" +
                //postgres
                "Введите пароль: \n" +
                //postgres
                "Подключение к базе данных выполнено\n" +
                "Введите команду:\n" +
                //drop test
                "Удаляем таблицу 'test'! Для подтверждения введите 'yes':\n" +
                //no
                "Удаление таблицы отменено\n" +
                "Введите команду:\n" +
                //drop test
                "Удаляем таблицу 'test'! Для подтверждения введите 'yes':\n" +
                //yes
                "Таблица test удалена\n" +
                "Введите команду:\n" +
                //create test (id int PRIMARY KEY NOT NULL, name text, password text)
                "Таблица test создана\n" +
                "Введите команду:\n" +
                //find test
                "------------------------\n" +
                "| id | name | password |\n" +
                "------------------------\n" +
                "Введите команду:\n" +
                "До свидания!\n", getData());
    }

    @Test
    public void testClearTable() {
        //given
        connectToDB();
        in.add("clear test");
        in.add("no");
        in.add("clear test");
        in.add("yes");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Введите название базы данных(sqlcmd): \n" +
                //sqlcmd
                "Введите имя пользователя: \n" +
                //postgres
                "Введите пароль: \n" +
                //postgres
                "Подключение к базе данных выполнено\n" +
                "Введите команду:\n" +
                //clear test
                "Удаляем все строки таблицы 'test'! Для подтверждения введите 'yes':\n" +
                //no
                "Удаление строк таблицы отменено\n" +
                "Введите команду:\n" +
                //clear test
                "Удаляем все строки таблицы 'test'! Для подтверждения введите 'yes':\n" +
                //yes
                "Все строки в таблице test удалены\n" +
                "Введите команду:\n" +
                "До свидания!\n", getData());
    }

    @Test
    public void testClearTableWrongInput() {
        //given
        connectToDB();
        in.add("clear");
        in.add("clear wrongtablename");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Введите название базы данных(sqlcmd): \n" +
                //sqlcmd
                "Введите имя пользователя: \n" +
                //postgres
                "Введите пароль: \n" +
                //postgres
                "Подключение к базе данных выполнено\n" +
                "Введите команду:\n" +
                //clear
                "Ошибка! Введено неправильное количество параметров команды clear \n" +
                "Введите команду:\n" +
                //clear wrongtablename
                "Ошибка! Нет такой таблицы. Доступны таблицы:\n" +
                "[test, users]\n" +
                "Введите команду:\n" +
                //exit
                "До свидания!\n", getData());
    }

    @Test
    public void testDropTableWrongInput() {
        //given
        connectToDB();
        in.add("drop");
        in.add("drop wrongtablename");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Введите название базы данных(sqlcmd): \n" +
                //sqlcmd
                "Введите имя пользователя: \n" +
                //postgres
                "Введите пароль: \n" +
                //postgres
                "Подключение к базе данных выполнено\n" +
                "Введите команду:\n" +
                //drop
                "Ошибка! Введено неправильное количество параметров команды drop \n" +
                "Введите команду:\n" +
                //drop wrongtablename
                "Ошибка! Нет такой таблицы. Доступны таблицы:\n" +
                "[test, users]\n" +
                "Введите команду:\n" +
                //exit
                "До свидания!\n", getData());
    }

    @Test
    public void testInsertAndFind() {
        //given
        connectToDB();
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
        assertEquals("Введите название базы данных(sqlcmd): \n" +
                //sqlcmd
                "Введите имя пользователя: \n" +
                //postgres
                "Введите пароль: \n" +
                //postgres
                "Подключение к базе данных выполнено\n" +
                "Введите команду:\n" +
                //clear test
                "Удаляем все строки таблицы 'test'! Для подтверждения введите 'yes':\n" +
                //yes
                "Все строки в таблице test удалены\n" +
                "Введите команду:\n" +
                //insert test
                "Введите данные в формате: field1 newValue1 field2 newValue2 ... \n" +
                //id 1 name Vasya password pass1
                "Строка добавлена\n" +
                "Введите команду:\n" +
                //insert test
                "Введите данные в формате: field1 newValue1 field2 newValue2 ... \n" +
                //id 2 name Petya password pass2
                "Строка добавлена\n" +
                "Введите команду:\n" +
                //find test
                "-------------------------\n" +
                "| id |  name | password |\n" +
                "-------------------------\n" +
                "|  1 | Vasya |    pass1 |\n" +
                "|  2 | Petya |    pass2 |\n" +
                "-------------------------\n" +
                "Введите команду:\n" +
                //find test 2 1
                "-------------------------\n" +
                "| id |  name | password |\n" +
                "-------------------------\n" +
                "|  2 | Petya |    pass2 |\n" +
                "-------------------------\n" +
                //exit
                "Введите команду:\n" +
                "До свидания!\n", getData());
    }

    @Test
    public void testFindWrongInput() {
        //given
        connectToDB();
        in.add("find");
        in.add("find wrongtablename");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Введите название базы данных(sqlcmd): \n" +
                //sqlcmd
                "Введите имя пользователя: \n" +
                //postgres
                "Введите пароль: \n" +
                //postgres
                "Подключение к базе данных выполнено\n" +
                "Введите команду:\n" +
                //find
                "Ошибка! Введено неправильное количество параметров команды find \n" +
                "Введите команду:\n" +
                //find wrongtablename
                "Ошибка! Нет такой таблицы. Доступны таблицы:\n" +
                "[test, users]\n" +
                "Введите команду:\n" +
                //exit
                "До свидания!\n", getData());
    }

    @Test
    public void testHelp() {
        //given
        connectToDB();
        in.add("help");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Введите название базы данных(sqlcmd): \n" +
                //sqlcmd
                "Введите имя пользователя: \n" +
                //postgres
                "Введите пароль: \n" +
                //postgres
                "Подключение к базе данных выполнено\n" +
                "Введите команду:\n" +
                //help
                "Существующие команды:\n" +
                "\thelp\n" +
                "\t\tвывести список команд\n" +
                "\texit\n" +
                "\t\tвыход из программы\n" +
                "\ttables\n" +
                "\t\tвывести список таблиц\n" +
                "\tfind tableName [LIMIT OFFET]\n" +
                "\t\tвывести содержимое таблицы [LIMIT - количество строк OFFSET - начальная строка]\n" +
                "\tupdate tableName ID\n" +
                "\t\tизменить строку таблицы tableName (ID - идентификатор строки)\n" +
                "\tinsert tableName\n" +
                "\t\tвставить строку в таблицу\n" +
                "\tclear tableName\n" +
                "\t\tочистить содержимое таблицы\n" +
                "\tcreate tableName ( columnName1 dataType1 [PRIMARY KEY] [NOT NULL], ... " +
                "columnNameN dataTypeN [NOT NULL] )\n" +
                "\t\tсоздать таблицу\n" +
                "\tdrop tableName\n" +
                "\t\tудалить таблицу\n" +
                "Введите команду:\n" +
                //exit
                "До свидания!\n", getData());
    }

    @Test
    public void testInsertWrongInput() {
        //given
        connectToDB();
        in.add("insert");
        in.add("insert wrongtablename");
        in.add("insert test");
        in.add("id 1 name");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Введите название базы данных(sqlcmd): \n" +
                //sqlcmd
                "Введите имя пользователя: \n" +
                //postgres
                "Введите пароль: \n" +
                //postgres
                "Подключение к базе данных выполнено\n" +
                "Введите команду:\n" +
                //insert
                "Ошибка! Введено неправильное количество параметров команды insert \n" +
                "Введите команду:\n" +
                //insert wrongtablename
                "Ошибка! Нет такой таблицы. Доступны таблицы:\n" +
                "[test, users]\n" +
                "Введите команду:\n" +
                //insert test
                "Введите данные в формате: field1 newValue1 field2 newValue2 ... \n" +
                //id 1 name
                "Ошибка! Нечетное количество параметров\n" +
                "Введите команду:\n" +
                //exit
                "До свидания!\n", getData());
    }

    @Test
    public void testTables() {
        //given
        Set<String> expectedTableNames = new HashSet<>(tablesWithoutTableTest);
        expectedTableNames.add("test");
        connectToDB();
        in.add("tables");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Введите название базы данных(sqlcmd): \n" +
                //sqlcmd
                "Введите имя пользователя: \n" +
                //postgres
                "Введите пароль: \n" +
                //postgres
                "Подключение к базе данных выполнено\n" +
                "Введите команду:\n" +
                //tables
                expectedTableNames.toString()+ "\n" +
                "Введите команду:\n" +
                "До свидания!\n", getData());
    }

    @Test
    public void testUnsupported() {
        //given
        connectToDB();
        in.add("wrongcommand");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Введите название базы данных(sqlcmd): \n" +
                //sqlcmd
                "Введите имя пользователя: \n" +
                //postgres
                "Введите пароль: \n" +
                //postgres
                "Подключение к базе данных выполнено\n" +
                "Введите команду:\n" +
                //wrongcommand
                "Введите правильно команду. (help - вывод списка команд)\n" +
                "Введите команду:\n" +
                "До свидания!\n", getData());
    }

    @Test
    public void testUpdate() {
        //given
        connectToDB();
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
        assertEquals("Введите название базы данных(sqlcmd): \n" +
                //sqlcmd
                "Введите имя пользователя: \n" +
                //postgres
                "Введите пароль: \n" +
                //postgres
                "Подключение к базе данных выполнено\n" +
                "Введите команду:\n" +
                //clear test
                "Удаляем все строки таблицы 'test'! Для подтверждения введите 'yes':\n" +
                //yes
                "Все строки в таблице test удалены\n" +
                "Введите команду:\n" +
                //insert test
                "Введите данные в формате: field1 newValue1 field2 newValue2 ... \n" +
                //id 1 name Vasya password pass1
                "Строка добавлена\n" +
                "Введите команду:\n" +
                //insert test
                "Введите данные в формате: field1 newValue1 field2 newValue2 ... \n" +
                //id 2 name Petya password pass2
                "Строка добавлена\n" +
                "Введите команду:\n" +
                //find test
                "-------------------------\n" +
                "| id |  name | password |\n" +
                "-------------------------\n" +
                "|  1 | Vasya |    pass1 |\n" +
                "|  2 | Petya |    pass2 |\n" +
                "-------------------------\n" +
                "Введите команду:\n" +
                //update test 1
                "-------------------------\n" +
                "| id |  name | password |\n" +
                "-------------------------\n" +
                "|  1 | Vasya |    pass1 |\n" +
                "-------------------------\n" +
                "Введите данные к изменению в формате: field1 newValue1 field2 newValue2 ... \n" +
                //id 1 name VasyaPupkin
                "Измененная строка:\n" +
                "-------------------------------\n" +
                "| id |        name | password |\n" +
                "-------------------------------\n" +
                "|  1 | VasyaPupkin |    pass1 |\n" +
                "-------------------------------\n" +
                //find test
                "Введите команду:\n" +
                "-------------------------------\n" +
                "| id |        name | password |\n" +
                "-------------------------------\n" +
                "|  2 |       Petya |    pass2 |\n" +
                "|  1 | VasyaPupkin |    pass1 |\n" +
                "-------------------------------\n" +
                //exit
                "Введите команду:\n" +
                "До свидания!\n", getData());
    }

    @Test
    public void testUpdateWrongInput() {
        //given
        connectToDB();
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
        assertEquals("Введите название базы данных(sqlcmd): \n" +
                //sqlcmd
                "Введите имя пользователя: \n" +
                //postgres
                "Введите пароль: \n" +
                //postgres
                "Подключение к базе данных выполнено\n" +
                "Введите команду:\n" +
                //update
                "Ошибка! Введено неправильное количество параметров команды update \n" +
                "Введите команду:\n" +
                //update wrongtablename 1
                "Ошибка! Нет такой таблицы. Доступны таблицы:\n" +
                "[test, users]\n" +
                "Введите команду:\n" +
                //update test 77
                "В таблице test нет строки с id: 77\n" +
                "Введите команду:\n" +
                //insert test
                "Введите данные в формате: field1 newValue1 field2 newValue2 ... \n" +
                //id 1 name Vasya password pass1
                "Строка добавлена\n" +
                "Введите команду:\n" +
                //update test 1
                "-------------------------\n" +
                "| id |  name | password |\n" +
                "-------------------------\n" +
                "|  1 | Vasya |    pass1 |\n" +
                "-------------------------\n" +
                "Введите данные к изменению в формате: field1 newValue1 field2 newValue2 ... \n" +
                //name
                "Ошибка! Нечетное количество параметров\n" +
                "Введите команду:\n" +
                //exit
                "До свидания!\n", getData());
    }

    private void connectToDB() {
        in.add("sqlcmd");
        in.add("postgres");
        in.add("postgres");
    }
}
