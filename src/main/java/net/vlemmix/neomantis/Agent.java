package net.vlemmix.neomantis;

import net.vlemmix.neomantis.utils.BitfinexAccount;
import net.vlemmix.neomantis.utils.SqliteDb;
import net.vlemmix.neomantis.utils.Wallet;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.Balance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;


import static java.sql.Types.NULL;

public class Agent implements Runnable {
    private int id;
    private LocalDateTime created;
    private String name;
    private String style;
    private String symbol;
    private int refreshPeriod;//renew Agent parameters
    private int updatePeriod;//update from exchange
    private int weight;
    private float baseBuyPercent;
    private float baseSellPercent;
    private float noiseBuyPercentOfBase;
    private float noiseSellPercentOfBase;
    // derived
    private float fractionOfBudget;
    //db
    private Connection connection;

    // Agent
    private int state;
    private NeoMantisOrder neoMantisOrder;

    private float totalBudget;

    Exchange exchange = BitfinexAccount.createExchange();

    private static final Logger log = LoggerFactory.getLogger(Agent.class);

    public Agent(int id, LocalDateTime created, String name, String style, String symbol, int refreshPeriod, int weight,
                 float fractionOfBudget, float baseBuyPercent, float baseSellPercent, float noiseBuyPercentOfBase,
                 float noiseSellPercentOfBase, int updatePeriod) {

        log.info("Agent....");

        this.id = id;
        this.created = created;
        this.name = name;
        this.style = style;
        this.symbol = symbol;
        this.refreshPeriod = refreshPeriod;
        this.updatePeriod = updatePeriod;
        this.weight = weight;
        this.baseBuyPercent = baseBuyPercent;
        this.baseSellPercent = baseSellPercent;
        this.noiseBuyPercentOfBase = noiseBuyPercentOfBase;
        this.noiseSellPercentOfBase = noiseSellPercentOfBase;
        this.fractionOfBudget = fractionOfBudget;
        System.out.println("*** NEW AGENT! ***");
        System.out.println("id: " + this.id);
        System.out.println("created: " + this.created + " = "
                + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(this.created));
        System.out.println("name: " + this.name);
        System.out.println("style: " + this.style);
        System.out.println("symbol: " + this.symbol);
        System.out.println("refreshPeriod: " + this.refreshPeriod);
        System.out.println("updatePeriod: " + this.updatePeriod);
        System.out.println("weight: " + this.weight);
        System.out.println("baseBuyPercent: " + this.baseBuyPercent);
        System.out.println("baseSellPercent: " + this.baseSellPercent);
        System.out.println("noiseBuyPercentOfBase: " + this.noiseBuyPercentOfBase);
        System.out.println("noiseSellPercentOfBase: " + this.noiseSellPercentOfBase);
        System.out.println("fractionOfBudget: " + this.fractionOfBudget);

        state = NeoMantisState.INIT;

        connection = SqliteDb.getInstance().getConnection("jdbc:sqlite:D:\\sqlite3dbs\\Mantis.db");

    }

    public synchronized void run() {


        while (true) {

            switch (state) {
                case NeoMantisState.INIT:
                    handleStateInit();
                    break;
                case NeoMantisState.MONITORING_ORDER:
                    handleMonitoringOrder();
                    break;
                case NeoMantisState.WAITING_FOR_PLACE_ORDER_TRIGGER:
                    handleWaitingForPlaceOrderTrigger();
                    break;

                default:
                    break;


            }


            try {
                {
                    System.out.println(LocalDateTime.now() + ":" + this + ": update" + name);
                    wait(updatePeriod);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void handleWaitingForPlaceOrderTrigger() {
        //TODO
        System.out.println("handleWaitingForPlaceOrderTrigger");
        //order placed
        state = NeoMantisState.MONITORING_ORDER;
    }

    private void handleMonitoringOrder() {
        //TODO
        System.out.println("handleMonitoringOrder");
        //if order executed
        state = NeoMantisState.WAITING_FOR_PLACE_ORDER_TRIGGER;
    }

    public void handleStateInit() {
        Statement statement;
        ResultSet rs;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US);
        // check if there is a NeoMantisOrder
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(
                    "SELECT ID,\r\n" +
                            "       IdCode,\r\n" +
                            "       Created,\r\n" +
                            "       Symbol,\r\n" +
                            "       Amount,\r\n" +
                            "       Price,\r\n" +
                            "       Side,\r\n" +
                            "       Type,\r\n" +
                            "       Status,\r\n" +
                            "       Base,\r\n" +
                            "       NoiseProcentDistance,\r\n" +
                            "       NoiseAbsoluteDistance,\r\n" +
                            "       startValue,\r\n" +
                            "       endValue,\r\n" +
                            "       deltaValue,\r\n" +
                            "       CreatedBy\r\n" +
                            "  FROM NeoMantisOrder\r\n" +
                            "  where CreatedBy = " + id + " and Status='Active';");

            if (rs.next()) { // 1 or none rows
                System.out.println("Agent found: " + rs.getString("ID"));
                //TODO: read NMOrder and create object

                //neoMantisOrder = new NeoMantisOrder(NULL,NULL,LocalDateTime.now(),symbol,3,);

            } else {
                System.out.println("Order for " + name + " not found");// so create one
                System.out.println("getting total budget");
                updateTotalBudget();
                state = NeoMantisState.WAITING_FOR_PLACE_ORDER_TRIGGER;


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTotalBudget() {
        log.info("updateTotalBudget");
        Wallet.getInstance().update();

        // test:
//        Map<Currency, Balance> balances = null;
//        try {
//            balances = exchange.getAccountService().getAccountInfo().getWallet().getBalances();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("BALANCE:");
//        System.out.println(balances);
//        System.out.println(balances.get(Currency.BTC));
//        Collection<Balance> bal=  balances.values();
//        for (int i =0; i<bal.size();i++){
//            System.out.println("nog een "+bal.toArray()[i]);
//            System.out.println("DUS in aantal: "+((Balance)bal.toArray()[i]).getAvailable()+ " "+((Balance)bal.toArray()[i]).getCurrency());
//            System.out.println("DUS in aantal: "+((Balance)bal.toArray()[i]).getAvailable()+ " "+((Balance)bal.toArray()[i]).getAvailable());
//        }

    }
}
