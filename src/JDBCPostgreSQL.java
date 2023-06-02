import java.security.SecureRandom;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class JDBCPostgreSQL {
    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/postgres";
    static final String USER = "postgres";
    static final String PASS = "1234";
    private static Statement statement;
    private static Connection connection = null;
    final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    final String NUMBER = "0123456789";
    final String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER;
    public SecureRandom random = new SecureRandom();
    public static void connectDataBase() throws SQLException {
        System.out.println("Testing connection to PostgreSQL JDBC");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return;
        }
        System.out.println("PostgreSQL JDBC Driver successfully connected");


        try {
            connection = DriverManager
                    .getConnection(DB_URL, USER, PASS);

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }
        if (connection != null) {
            System.out.println("You successfully connected to database now");
        } else {
            System.out.println("Failed to make connection to database");
        }
        statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
    }

    public void createTable() throws SQLException {
        statement.executeQuery("CREATE TABLE IF NOT EXISTS people\n" +
                "(\n" +
                "    id SERIAL PRIMARY KEY,\n" +
                "    firstName CHARACTER VARYING(30),\n" +
                "    lastName CHARACTER VARYING(30),\n" +
                "    familyName CHARACTER VARYING(30), \n" +
                "    gender CHARACTER VARYING(30),\n" +
                "    age CHARACTER VARYING(30)\n" +
                ");");

        //System.out.println(res);
        connection.close();
        statement.close();
    }
    public void addNetwork(String firstName, String lastName, String familyName, String date, String gender) throws SQLException {
        String query = "INSERT INTO people (firstname, lastname, familyname, gender, age) VALUES ( ";
        query += "' " + firstName + "', ";
        query += "' " + lastName + "', ";
        query += "' " + familyName + "', ";
        query += "' " + gender + "', ";
        query += "' " + date + "') ";

        statement.executeQuery(query);
        connection.close();
        statement.close();
    }
    public void selectDistinct() throws SQLException {
        String query = "SELECT DISTINCT firstName, lastname, familyName, age FROM people ORDER BY firstName, lastname, familyName";
        ResultSet res = statement.executeQuery(query);
        int j = 1;
        while(res.next()) {
            System.out.println("id= " + j);
            for (int i = 1; i <= res.getMetaData().getColumnCount(); i++) {
                System.out.println(res.getMetaData().getColumnName(i) + res.getString(i) + "\t");
                 if(i == res.getMetaData().getColumnCount()) {
                   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                   String cur = res.getString(i);
                    cur = cur.replaceAll(" ", "");
                    LocalDate startDate = LocalDate.parse(cur, formatter);
                    LocalDate endDate = LocalDate.parse(LocalDate.now().format(formatter), formatter);
                    Period period = Period.between(startDate, endDate);
                    System.out.println("Возраст " + period.getYears());
                }
            }
            j++;
        }
        connection.close();
        statement.close();

    }

    public void randomInsert() throws SQLException {
        System.out.println("ПРИМЕРНОЕ ВРЕМЯ ОЖИДАНИЯ 4 МИНУТЫ");
        long m = System.currentTimeMillis();
        for(int i = 0; i < 1000000; i++) {
            String query = "INSERT INTO people (firstname, lastname, familyname, gender, age) VALUES (";
            query += "' " + generateRandomString(6) + "', ";
            query += "' " + generateRandomString(5) + "', ";
            query += "' " + generateRandomString(6) + "', ";
            query += "' " + generateRandomString(1) + "', ";
            query += "' " +generateRandomString(4)+ "') ";
            statement.execute(query);
        }

        for(int i = 0; i < 100; i++) {
            String query = "INSERT INTO people (firstname, lastname, familyname, gender, age) VALUES (";
            query += "' F" + generateRandomString(6) + "', ";
            query += "' " + generateRandomString(5) + "', ";
            query += "' " + generateRandomString(6) + "', ";
            query += "' " + generateRandomString(1) + "', ";
            query += "' " +generateRandomString(4)+ "') ";
            statement.execute(query);
        }
        connection.close();
        statement.close();
        System.out.println((System.currentTimeMillis() - m)/60000);
    }

    public void selectRandomInput() throws SQLException {
        long m = System.currentTimeMillis();
        String query = "SELECT firstName FROM people WHERE firstname LIKE ' F%' and gender = ' М'";
        ResultSet res = statement.executeQuery(query);
        int j = 1;
        while(res.next()) {
            System.out.println("id= " + j);
            for (int i = 1; i <= res.getMetaData().getColumnCount(); i++) {
                System.out.println(res.getMetaData().getColumnName(i) + res.getString(i) + "\t");
            }
            j++;
        }
        System.out.println("ВРЕМЯ ВЫПОЛНЕНИЯ В МИЛЛИСЕКУНДАХ: "+(System.currentTimeMillis() - m));
    }
    public  String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        if (length < 1) throw new IllegalArgumentException();
        else if(length == 1) {
            final String DATA_FOR_RANDOM_STRING_ = "МЖ";
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING_.length());
            char rndChar = DATA_FOR_RANDOM_STRING_.charAt(rndCharAt);
            sb.append(rndChar);
            return sb.toString();
        }
        else if(length == 4) {
                RandomDates rdm = new RandomDates();
                String res = rdm.createRandomDate(1900, 2023);
                sb.append(res);
                return sb.toString();
        }
        for (int i = 0; i < length; i++) {
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);
            sb.append(rndChar);

        }
        return sb.toString();

    }

    public class RandomDates {

        public  int createRandomIntBetween(int start, int end) {
            return start + (int) Math.round(Math.random() * (end - start));
        }

        public  String createRandomDate(int startYear, int endYear) {
            int day = createRandomIntBetween(1, 28);
            int month = createRandomIntBetween(1, 12);
            int year = createRandomIntBetween(startYear, endYear);
            String res = "";
            if(day > 9) {
                res += day + ".";
            } else {
                res += "0" + day + ".";
            }
           if(month > 9) {
               res += month + ".";
           } else {
               res += "0" + month + ".";
           }
            res += year;
            return res;
        }
    }

}
