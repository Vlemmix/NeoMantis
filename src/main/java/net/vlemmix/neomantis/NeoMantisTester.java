package net.vlemmix.neomantis;

import org.knowm.xchange.Exchange;

import net.vlemmix.playground.DbSelect;

public class NeoMantisTester {
    //private TickerGrabber BtcUsdGrabber;

    public static void main(String[] args) {

//		Thread btcUsdGrabber=new Thread(new TickerGrabber("btc_usd", 2*1000));
//		Thread iotaUsdGrabber=new Thread(new TickerGrabber("iota_usd", 1*1000));
//		Thread ethUsdGrabber=new Thread(new TickerGrabber("eth_usd", (int) (1.5*1000)));
//		btcUsdGrabber.start();
//		iotaUsdGrabber.start();
//		ethUsdGrabber.start();

        DbSelect test = new DbSelect();
        test.select();

        //

    }


}
