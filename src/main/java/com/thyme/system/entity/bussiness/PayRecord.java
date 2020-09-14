package com.thyme.system.entity.bussiness;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Arslinth
 * @ClassName PayRecord
 * @Description TODO
 * @Date 2020/6/19
 */
@Data
public class PayRecord {

    @TableId
    private String id;

    private String principalId;

    private double pay;

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date payDate;

    private String payment;

    public PayRecord() {
    }

    public PayRecord(String id, String principalId, double pay, Date payDate, String payment) {
        this.id = id;
        this.principalId = principalId;
        this.pay = pay;
        this.payDate = payDate;
        this.payment = payment;
    }
}
