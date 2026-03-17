package esports.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton that holds the single JDBC connection to MySQL.
 *
 * Configure your MySQL credentials in the three constants below,
 * or change them at runtime before the first call to getInstance().
 */
public class DatabaseConnection {

    // ── Change these to match your MySQL Workbench setup ──────────────────
    private static final String DB_URL      = "jdbc:mysql://localhost:3306/esports_db"
                                            + "?useSSL=false"
                                            + "&allowPublicKeyRetrieval=true"
                                            + "&serverTimezone=UTC";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "123456789";        // ← your MySQL root password
    // ─────────────────────────────────────────────────────────────────────

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("[DB] Connected to MySQL: esports_db");
        } catch (ClassNotFoundException e) {
            System.err.println("[DB] MySQL JDBC driver not found. " +
                "Make sure mysql-connector-java-8.0.33.jar is in the lib/ folder.");
            throw new RuntimeException("JDBC driver missing", e);
        } catch (SQLException e) {
            System.err.println("[DB] Connection failed: " + e.getMessage());
            System.err.println("[DB] Check: Is MySQL running? Is the password correct? Did you run schema.sql?");
            throw new RuntimeException("Database connection failed", e);
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            // Auto-reconnect if connection was dropped
            if (connection == null || connection.isClosed() || !connection.isValid(3)) {
                System.out.println("[DB] Reconnecting...");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to reconnect to database", e);
        }
        return connection;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DB] Connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
