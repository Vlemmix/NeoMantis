package net.vlemmix.neomantis;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Agent_BU_ {
    private int id;
    private LocalDateTime created;
    private String name;
    private String style;
    private String symbol;
    private int refreshPeriod;
    private int weight;
    private float baseBuyPercent;
    private float baseSellPercent;
    private float noiseBuyPercentOfBase;
    private float noiseSellPercentOfBase;
    // derived
    private float fractionOfBudget;

    public Agent_BU_(int id, LocalDateTime created, String name, String style, String symbol, int refreshPeriod, int weight,
                     float fractionOfBudget, float baseBuyPercent, float baseSellPercent, float noiseBuyPercentOfBase, float noiseSellPercentOfBase) {
        this.id = id;
        this.created = created;
        this.name = name;
        this.style = style;
        this.symbol = symbol;
        this.refreshPeriod = refreshPeriod;
        this.weight = weight;
        this.baseBuyPercent = baseBuyPercent;
        this.baseSellPercent = baseSellPercent;
        this.noiseBuyPercentOfBase = noiseBuyPercentOfBase;
        this.noiseSellPercentOfBase = noiseSellPercentOfBase;
        this.fractionOfBudget = fractionOfBudget;
        System.out.println("*** NEW AGENT! ***");
        System.out.println("id: " + this.id);
        System.out.println("created: " + this.created + " = " + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(this.created));
        System.out.println("name: " + this.name);
        System.out.println("style: " + this.style);
        System.out.println("symbol: " + this.symbol);
        System.out.println("refreshPeriod: " + this.refreshPeriod);
        System.out.println("weight: " + this.weight);
        System.out.println("baseBuyPercent: " + this.baseBuyPercent);
        System.out.println("baseSellPercent: " + this.baseSellPercent);
        System.out.println("noiseBuyPercentOfBase: " + this.noiseBuyPercentOfBase);
        System.out.println("noiseSellPercentOfBase: " + this.noiseSellPercentOfBase);
        System.out.println("fractionOfBudget: " + this.fractionOfBudget);
    }

}
