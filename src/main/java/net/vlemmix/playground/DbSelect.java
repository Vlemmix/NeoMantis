package net.vlemmix.playground;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.vlemmix.neomantis.utils.SqliteDb;

public class DbSelect {
    private Connection connection = SqliteDb.getInstance().getConnection("jdbc:sqlite:D:\\sqlite3dbs\\Mantis.db");
    private Statement statement;

    public void select() {
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Ticker;");
            while (rs.next()) {
                System.out.println("ok");
                System.out.println(rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
