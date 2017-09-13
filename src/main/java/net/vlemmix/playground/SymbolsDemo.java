package net.vlemmix.playground;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitfinex.v1.BitfinexExchange;
import org.knowm.xchange.bitfinex.v1.service.BitfinexMarketDataServiceRaw;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.service.marketdata.MarketDataService;

public class SymbolsDemo {

    public static void main(String[] args) throws Exception {

        // Use the factory to get Bitfinex exchange API using default settings
        Exchange bitfinex = ExchangeFactory.INSTANCE.createExchange(BitfinexExchange.class.getName());

        // Interested in the public market data feed (no authentication)
        MarketDataService marketDataService = bitfinex.getMarketDataService();

        generic(bitfinex);
        raw((BitfinexMarketDataServiceRaw) marketDataService);
        dbConnect();
        readDB();
    }

    private static void generic(Exchange bitfinex) {

        System.out.println(bitfinex.getExchangeSymbols().toString());

    }

    private static void raw(BitfinexMarketDataServiceRaw marketDataService) throws IOException {

        Collection<String> symbols = marketDataService.getBitfinexSymbols();
        System.out.println(symbols);

        Collection<CurrencyPair> currencyPairs = marketDataService.getExchangeSymbols();
        System.out.println("EVxxx:" + currencyPairs);
    }

    private static void dbConnect() {
        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:testdb1");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    private static void readDB() throws SQLException {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:D:\\sqlite3dbs\\testdb1.db");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Ticker;");
        int i = 0;
        while (rs.next()) {
            System.out.println("ok" + i++);

        }
        rs.close();
        stmt.close();
        c.close();


    }
}
