import java.sql.*;

public class JDBC {

    private static final String url = "jdbc:mysql://localhost:3306/?user=root?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=utf-8";
    private static final String user = "root";
    private static final String password = "738622222:AAGaJUjwC0-M_dgNUnfUVLPKItOwgd0i37M";
    private static Connection con;
    private static Statement stmt;


    public static void command(String command){
        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            stmt.execute(command);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet execute(String command){
        ResultSet resultSet = null;
        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            resultSet = stmt.executeQuery(command);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static void createTables(){
        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            String createShema = "CREATE SCHEMA Students DEFAULT CHARACTER SET utf8";
            String createTable_student = "CREATE TABLE Students.student " +
                    "(id INT NOT NULL AUTO_INCREMENT, " +
                    "firstName VARCHAR(45) NOT NULL, " +
                    "lastName VARCHAR(45) NOT NULL, " +
                    "groupName VARCHAR(45) NOT NULL, " +
                    "isHeadman TINYINT(45) NOT NULL, " +
                    "professor VARCHAR(45) NOT NULL, " +
                    "isEmpty TINYINT(45) NOT NULL, " +
                    "feature VARCHAR(45) NOT NULL, " +
                    "votes INT(11) NOT NULL, PRIMARY KEY(id))";
            String createTable_professor = "CREATE TABLE Students.professor " +
                    "(id INT NOT NULL AUTO_INCREMENT, " +
                    "firstName VARCHAR(45) NOT NULL, " +
                    "lastName VARCHAR(45) NOT NULL, " +
                    "groupName VARCHAR(45) NOT NULL, PRIMARY KEY (id))";
            //stmt.executeUpdate("drop database Students");
            stmt.executeUpdate(createShema);
            stmt.executeUpdate(createTable_student);
            stmt.executeUpdate(createTable_professor);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
