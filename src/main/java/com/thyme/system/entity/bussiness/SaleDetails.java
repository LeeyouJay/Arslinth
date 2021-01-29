package com.thyme.system.entity.bussiness;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaleDetails {

    private String saleId;

    private String pdName;

    private double num;

    private double price;

    private double cost;

    private String type;

    private double priceTotal;

    private double costTotal;

    public SaleDetails() {
    }

    public SaleDetails(String saleId, String pdName, double num, double price, double cost, String type, double priceTotal, double costTotal) {
        this.saleId = saleId;
        this.pdName = pdName;
        this.num = num;
        this.price = price;
        this.cost = cost;
        this.type = type;
        this.priceTotal = priceTotal;
        this.costTotal = costTotal;
    }
}
