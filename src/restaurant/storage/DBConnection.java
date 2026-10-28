
/*package restaurant.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:postgresql://localhost:5432/RMS_db";

    private static final String USER = "postgres";

    // Enter your PostgreSQL password here
    private static final String PASSWORD = "#Shu9390";

    private DBConnection() {
        // Prevent object creation
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}*/

package restaurant.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:postgresql://localhost:5432/RMS_db";

    private static final String USER = "postgres";

    // Read the password from an environment variable
    private static final String PASSWORD =
            System.getenv("RMS_DB_PASSWORD");

    private DBConnection() {
        // Prevent object creation
    }

    public static Connection getConnection() throws SQLException {

        if (PASSWORD == null || PASSWORD.isBlank()) {
            throw new SQLException(
                    "RMS_DB_PASSWORD is not configured.");
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}


