package net.vlemmix.neomantis;

public class TickerGrabber implements Runnable {
    private String currencyPair;
    private int refreshPeriod;

    public TickerGrabber(String currencyPair, int refreshPeriod) {
        this.currencyPair = currencyPair;
        this.refreshPeriod = refreshPeriod;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(refreshPeriod);
                System.out.println("test " + currencyPair + " " + refreshPeriod + " " + System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }

        }

    }

}
