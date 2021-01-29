package com.thyme.system.entity.bussiness;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaleList {

    private String id;

    private String consumer;

    private String payType;

    private String phone;

    private double totalPrice;

    private double totalCost;

    private String createTime;

    @TableLogic
    private boolean isDeleted;

    public SaleList() {
    }

    public SaleList(String id, String consumer, String payType, String phone, double totalPrice, double totalCost, String createTime, boolean isDeleted) {
        this.id = id;
        this.consumer = consumer;
        this.payType = payType;
        this.phone = phone;
        this.totalPrice = totalPrice;
        this.totalCost = totalCost;
        this.createTime = createTime;
        this.isDeleted = isDeleted;
    }
}
