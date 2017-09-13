package net.vlemmix.neomantis;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.logging.Logger;

import net.vlemmix.neomantis.utils.SqliteDb;
import net.vlemmix.playground.DbSelect;

public class Director {
    private Connection connection;
    private Agent agent1;
    private float totalAgentsWeight;

    public Director(String dbFile) {
        connection = SqliteDb.getInstance().getConnection("jdbc:sqlite:D:\\sqlite3dbs\\Mantis.db");
    }

    public void startAll() {

        totalAgentsWeight = getTotalAgentsWeight();

        agent1 = getAgent("IOTA Limit Basic 0510");
        agent1.run();
//		TickerGrabber tg = new TickerGrabber("fake", 2000);
//		tg.run();

    }

    private Agent getAgent(String name) {
        Agent agent = null;
        Statement statement;
        ResultSet rs;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US);

        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(
                    "SELECT Id, Created, Name, Style, Symbol, RefreshPeriod, Weight, BaseBuyPercent, BaseSellPercent, NoiseBuyPercentOfBase, NoiseSellPercentOfBase, UpdatePeriod FROM Agent WHERE Name = '"
                            + name + "';");

            if (rs.next()) { // 1 or none rows
                System.out.println("Director found: " + rs.getString("name"));
                agent = new Agent(rs.getInt("id"), LocalDateTime.parse(rs.getString("created"), formatter),
                        rs.getString("name"), rs.getString("style"), rs.getString("symbol"), rs.getInt("refreshPeriod"),
                        rs.getInt("weight"), (rs.getInt("weight") / totalAgentsWeight), rs.getFloat("baseBuyPercent"),
                        rs.getFloat("baseSellPercent"), rs.getFloat("noiseBuyPercentOfBase"),
                        rs.getFloat("noiseSellPercentOfBase"), rs.getInt("updatePeriod")
                );
            } else {
                System.out.println(name + " not found");
            }
            rs.close();
            statement.close();
            // keep singleton connection open
            // connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return agent;
    }

    private int getTotalAgentsWeight() {
        Statement statement;
        ResultSet rs;
        int totalWeight;
        // get total agent weight from DB
        totalWeight = 0;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT Weight FROM Agent;");
            while (rs.next()) {
                totalWeight = totalWeight + rs.getInt(1);
            }
            rs.close();
            statement.close();
            // keep singleton connection open
            // connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalWeight;
    }

}
