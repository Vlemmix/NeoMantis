package net.vlemmix.neomantis;

import java.util.logging.Logger;

import net.vlemmix.neomantis.utils.SqliteDb;
import net.vlemmix.playground.DbSelect;

public class NeoMantis {

    public static void main(String[] args) {

        Director director = new Director("jdbc:sqlite:D:\\sqlite3dbs\\Mantis.db");

        director.startAll();

        // Thread btcUsdGrabber=new Thread(new TickerGrabber("btc_usd", 2*1000));
        // Thread iotaUsdGrabber=new Thread(new TickerGrabber("iota_usd", 1*1000));
        // Thread ethUsdGrabber=new Thread(new TickerGrabber("eth_usd", (int)
        // (1.5*1000)));
        // btcUsdGrabber.start();
        // iotaUsdGrabber.start();
        // ethUsdGrabber.start();

        // DbSelect test = new DbSelect();
        // test.select();

    }

}
