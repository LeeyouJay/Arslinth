package com.thyme.system.entity.bussiness;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Builder;
import lombok.Data;

/**
 * @author Arslinth
 * @ClassName OrderList
 * @Description TODO
 * @Date 2020/7/11
 */
@Data
@Builder
public class OrderList {
    private String id;

    private String consumer;

    private String payType;

    private String region;

    private String phone;

    private String checker;

    private double totalPrice;

    private String createTime;

    @TableLogic
    private int isDeleted;

    public OrderList() {
    }

    public OrderList(String id, String consumer, String payType, String region, String phone, String checker, double totalPrice, String createTime, int isDeleted) {
        this.id = id;
        this.consumer = consumer;
        this.payType = payType;
        this.region = region;
        this.phone = phone;
        this.checker = checker;
        this.totalPrice = totalPrice;
        this.createTime = createTime;
        this.isDeleted = isDeleted;
    }
}
