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

    private String remark;

    private String createTime;

    private String record;

    @TableLogic
    private boolean isDeleted;

    private String updateTime;

    public SaleList() {
    }

    public SaleList(String id, String consumer, String payType, String phone, double totalPrice, double totalCost, String remark, String createTime, String record, boolean isDeleted, String updateTime) {
        this.id = id;
        this.consumer = consumer;
        this.payType = payType;
        this.phone = phone;
        this.totalPrice = totalPrice;
        this.totalCost = totalCost;
        this.remark = remark;
        this.createTime = createTime;
        this.record = record;
        this.isDeleted = isDeleted;
        this.updateTime = updateTime;
    }
}
