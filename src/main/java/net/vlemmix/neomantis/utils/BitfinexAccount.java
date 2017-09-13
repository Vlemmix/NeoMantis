package net.vlemmix.neomantis.utils;

import net.vlemmix.neomantis.Agent;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.bitfinex.v1.BitfinexExchange;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class BitfinexAccount {
    private static Connection connection;


    public static Exchange createExchange() {
        String exchange = "Bitfinex";
        connection = SqliteDb.getInstance().getConnection("jdbc:sqlite:D:\\sqlite3dbs\\Mantis.db");
        // Use the factory to get BFX exchange API using default settings
        Exchange bfx = ExchangeFactory.INSTANCE.createExchange(BitfinexExchange.class.getName());

        ExchangeSpecification bfxSpec = bfx.getDefaultExchangeSpecification();

        Agent agent = null;
        Statement statement;
        ResultSet rs;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US);

        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(
                    "SELECT id,\n" +
                            "       APIkey,\n" +
                            "       SecretKey,\n" +
                            "       Exchange\n" +
                            "  FROM Account WHERE Exchange = '" + exchange + "';");

            if (rs.next()) { // 1 or none rows
                System.out.println("Account found: " + rs.getString("Exchange") + "with apikey:" + rs.getString("APIkey") + " SecretKey:" + rs.getString("SecretKey"));
                bfxSpec.setApiKey(rs.getString("APIkey"));
                bfxSpec.setSecretKey(rs.getString("SecretKey"));

            } else {
                System.out.println(exchange + " not found");
            }
            rs.close();
            statement.close();
            // keep singleton connection open
            // connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        bfx.applySpecification(bfxSpec);

        return bfx;
    }

}
