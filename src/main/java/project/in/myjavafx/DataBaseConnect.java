package project.in.myjavafx;
import java.sql.*;

//This class is responsible for connecting the database to the application
public class DataBaseConnect {

    //static variable to store the URL
    private static final String URL = "jdbc:postgresql://localhost:5432/KLSGit";
    //static variable to store the user-name
    private static final String USER = "postgres";
    //static variable to store the User-Password
    private static final String PASSWORD = "triveni";

    //This method is responsible for creating a connection to the Postgresql database
    public static Connection getConnection() throws SQLException {

        //Here we are making the connection by passing the URL,user-name,user-password
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

        //connected
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("SET search_path TO public");
        }
        //returning the connection object
        return conn;
    }

}
