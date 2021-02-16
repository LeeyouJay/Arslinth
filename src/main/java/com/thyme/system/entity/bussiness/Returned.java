package com.thyme.system.entity.bussiness;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Returned {
    private String id;

    private String principalId;

    private String productId;

    private String pcpName;

    private String pdName;

    private double cost;

    private int count;

    private String remark;

    private double totalCost;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
   // @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String createTime;

    public Returned() {
    }

    public Returned(String id, String principalId, String productId, String pcpName, String pdName, double cost, int count, String remark, double totalCost, String createTime) {
        this.id = id;
        this.principalId = principalId;
        this.productId = productId;
        this.pcpName = pcpName;
        this.pdName = pdName;
        this.cost = cost;
        this.count = count;
        this.remark = remark;
        this.totalCost = totalCost;
        this.createTime = createTime;
    }
}
