package com.thyme.system.entity.bussiness;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * @author Arslinth
 * @ClassName remittance
 * @Description TODO
 * @Date 2020/6/19
 */
@Data
@SuperBuilder
public class Remittance {

    @TableId
    private String id;

    private String principalId;

    private double totalCost;

    private int totalCount;

    private double totalPay;

    private double debt;

    public Remittance() {

    }

    public Remittance(String id, String principalId, double totalCost, int totalCount, double totalPay, double debt) {
        this.id = id;
        this.principalId = principalId;
        this.totalCost = totalCost;
        this.totalCount = totalCount;
        this.totalPay = totalPay;
        this.debt = debt;
    }
}
