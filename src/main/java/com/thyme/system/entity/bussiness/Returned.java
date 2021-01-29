package com.thyme.system.entity.bussiness;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Returned {
    private String id;

    private String principalId;

    private String productId;

    private double cost;

    private int count;

    private String remark;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    public Returned() {
    }

    public Returned(String id, String principalId, String productId, double cost, int count, String remark, Date createTime) {
        this.id = id;
        this.principalId = principalId;
        this.productId = productId;
        this.cost = cost;
        this.count = count;
        this.remark = remark;
        this.createTime = createTime;
    }
}
