package com.thyme.system.entity.bussiness;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Builder;
import lombok.Data;

/**
 * @author Arslinth
 * @ClassName OrderDetails
 * @Description TODO
 * @Date 2020/7/11
 */
@Data
@Builder
public class OrderDetails {
    private String id;

    private String pdName;

    private double value;

    private double price;

    private double subtotal;

    @TableLogic
    private int isDeleted;

    public OrderDetails() {
    }

    public OrderDetails(String id, String pdName, double value, double price, double subtotal, int isDeleted) {
        this.id = id;
        this.pdName = pdName;
        this.value = value;
        this.price = price;
        this.subtotal = subtotal;
        this.isDeleted = isDeleted;
    }
}
