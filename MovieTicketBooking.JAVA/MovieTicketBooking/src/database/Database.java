package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/movie_booking";
    private static final String USER = "root"; // kendi kullanıcı adın
    private static final String PASSWORD = "0234Et0234"; // kendi şifren

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
