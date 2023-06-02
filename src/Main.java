import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        JDBCPostgreSQL psgl = new JDBCPostgreSQL();
        try {
            psgl.connectDataBase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(args[0].equals("1")) {
            try {
                psgl.createTable();
            } catch (SQLException e){}
        } else if(args[0].equals("2")) {
            try {
                psgl.addNetwork(args[1], args[2], args[3],args[4], args[5]);
            } catch (SQLException e) {}
        } else if (args[0].equals("3")) {
            try {
                psgl.selectDistinct();
            } catch (SQLException e) {}
        } else if(args[0].equals("4")) {
            try {
                psgl.randomInsert();
            } catch (SQLException e) {}
        } else if(args[0].equals("5")) {
            try {
                psgl.selectRandomInput();
            } catch (SQLException e) {}
        }

    }
}