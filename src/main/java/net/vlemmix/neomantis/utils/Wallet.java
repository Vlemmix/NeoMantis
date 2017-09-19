//SELECT Id, Created, Type, Currency, Total, TotalInUSD, Available, AvailableInUSD, Frozen, FrozenInUSD FROM Wallet order by created desc limit 1

package net.vlemmix.neomantis.utils;


import net.vlemmix.neomantis.Agent;
import net.vlemmix.neomantis.NeoMantisState;
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
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Wallet {
    private ArrayList<String> symbolList = new ArrayList<String>();
    private static final Logger log = LoggerFactory.getLogger(Wallet.class);

    private static Wallet walletInstance;
    private Connection connection = SqliteDb.getInstance().getConnection("jdbc:sqlite:D:\\sqlite3dbs\\Mantis.db");

    private Wallet() {
        // private, go via getInstance plz.
    }

    public static Wallet getInstance() {
        if (walletInstance == null) {
            walletInstance = new Wallet();
        }
        return walletInstance;
    }

    public void update() {
        symbolList.clear();
        log.info("update");
        // get all used currencies from Agent DB
        Statement statement;
        ResultSet rs;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US);

        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(
                    "SELECT distinct Symbol FROM Agent where symbol is not null;");

            while (rs.next()) { // 1 or none rows
                System.out.println("symbol found: " + rs.getString("Symbol"));
                symbolList.add(rs.getString("Symbol"));

                //neoMantisOrder = new NeoMantisOrder(NULL,NULL,LocalDateTime.now(),symbol,3,);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // symbollist (from agent) filled.

        // get all Balances from bitfinex and store the used ones in DB
        log.info("list of symbols to get");
        Iterator itr = symbolList.iterator();
        while (itr.hasNext()) {
            log.info((String) itr.next());
        }
        Map<Currency, Balance> balances = null;
        Exchange exchange = BitfinexAccount.createExchange();
        try {
            balances = exchange.getAccountService().getAccountInfo().getWallet().getBalances();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collection<Balance> bal = balances.values();
        for (int i = 0; i < bal.size(); i++) {
            // check all from Balance;

            //System.out.println("nog een "+bal.toArray()[i]);
            //System.out.println("DUS van "+((Balance)bal.toArray()[i]).getCurrency()+" heb ik "+((Balance)bal.toArray()[i]).getAvailable()+ "!");

//            if (((Balance)bal.toArray()[i]).getCurrency().toString().compareToIgnoreCase("IOT")==0){
//                System.out.println("iot gevonden ("+((Balance)bal.toArray()[i]).getAvailable()+")");
//            }
//            else {
//                System.out.println("iot niet gevonden");
//            }
            if (symbolList.contains(((Balance) bal.toArray()[i]).getCurrency().toString())) {
                System.out.println("put " + ((Balance) bal.toArray()[i]).getCurrency().toString() + " in DB");
                try {
                    statement = connection.createStatement();

                    statement.executeUpdate(
                            "INSERT INTO Wallet (   Type, Currency, Total, TotalInUSD, Available, AvailableInUSD, Frozen, FrozenInUSD ) VALUES (  'Type', 'IOT', '1', '1', '1', '1', '1', '1' );");
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else {
                log.warn("Weird... " + ((Balance) bal.toArray()[i]).getCurrency().toString() + " is in wallet, but no agent configured...");
            }
        }
    }
}
