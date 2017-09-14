package net.vlemmix.neomantis;

import java.time.LocalDateTime;

public class NeoMantisOrder {
    private int ID;
    private String idCode;
    private LocalDateTime created;
    private String symbol;
    private float amount;
    private float price;
    private String side;
    private String type;
    private String status;
    private float base;
    private float noiseProcentDistance;
    private float noiseAbsoluteDistance;
    private float startValue;
    private float endValue;
    private float deltaValue;
    private int createdBy;

    public NeoMantisOrder(int ID, String idCode, LocalDateTime created, String symbol, float amount, float price, String side, String type, String status, float base
            , float noiseProcentDistance, float noiseAbsoluteDistance, float startValue, float endValue, float deltaValue, int createdBy) {
        this.ID = ID;
        this.created = created;
        this.symbol = symbol;
        this.amount = amount;
        this.price = price;
        this.side = side;
        this.type = type;
        this.status = status;
        this.base = base;
        this.noiseProcentDistance = noiseProcentDistance;
        this.noiseAbsoluteDistance = noiseAbsoluteDistance;
        this.startValue = startValue;
        this.endValue = endValue;
        this.deltaValue = deltaValue;
        this.createdBy = createdBy;

        //


    }

}
