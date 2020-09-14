package com.thyme.system.entity.bussiness;

import lombok.Builder;
import lombok.Data;

/**
 * @author Arslinth
 * @ClassName Product
 * @Description TODO
 * @Date 2020/6/17
 */
@Data
@Builder
public class Product {

    private String id;

    private String pdName;

    private String type;

    private double price;

    private double cost;

    private int period;

    private double yield;

    private double height;

    private double num;

    private boolean isShow;

    private String characters;

    public Product() {
    }

    public Product(String id, String pdName, String type, double price, double cost, int period, double yield, double height, double num, boolean isShow, String characters) {
        this.id = id;
        this.pdName = pdName;
        this.type = type;
        this.price = price;
        this.cost = cost;
        this.period = period;
        this.yield = yield;
        this.height = height;
        this.num = num;
        this.isShow = isShow;
        this.characters = characters;
    }
}
