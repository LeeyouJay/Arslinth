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

    private int periodMin;

    private int periodMax;

    private double yield;

    private double height;

    private double num;

    private String unit;

    private boolean isShow;

    private String characters;

    public Product() {
    }

    public Product(String id, String pdName, String type, double price, double cost, int periodMin, int periodMax, double yield, double height, double num, String unit, boolean isShow, String characters) {
        this.id = id;
        this.pdName = pdName;
        this.type = type;
        this.price = price;
        this.cost = cost;
        this.periodMin = periodMin;
        this.periodMax = periodMax;
        this.yield = yield;
        this.height = height;
        this.num = num;
        this.unit = unit;
        this.isShow = isShow;
        this.characters = characters;
    }
}
