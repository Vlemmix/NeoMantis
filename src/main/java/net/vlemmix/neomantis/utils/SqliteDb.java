package net.vlemmix.neomantis.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteDb {

    private static SqliteDb dbInstance;
    private static Connection connection;

    private SqliteDb() {
        // private, go via getInstance plz.
    }

    public static SqliteDb getInstance() {
        if (dbInstance == null) {
            dbInstance = new SqliteDb();
        }
        return dbInstance;
    }

    public Connection getConnection(String dbFile) {
        if (connection == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(dbFile);
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
        return connection;
    }

    public void close() {
        // why would you need this?

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            connection = null;
        }
    }
}
