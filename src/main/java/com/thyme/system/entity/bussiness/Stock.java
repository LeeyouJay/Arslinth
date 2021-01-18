package com.thyme.system.entity.bussiness;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.SuperBuilder;


import java.io.Serializable;
import java.util.Date;


/**
 * @author Arslinth
 * @ClassName Stock
 * @Description TODO
 * @Date 2020/6/11
 */
@Data
@SuperBuilder
public class Stock implements Serializable {

    static final long serialVersionUID = 1L;

    private String id;

    private String productId;

    private String principalId;

    private String type;

    @ExcelProperty(value = "数量(包)")
    private int count;

    @ExcelProperty(value = "规格")
    @TableField(exist = false)
    private String standards;

    @ExcelProperty(value = "单价")
    private double cost;

    @ExcelProperty(value = "零售价")
    private double price;

    @ExcelProperty(value = "日期")
    @DateTimeFormat("yyyy-MM-dd")
    private String inDate;

    @ExcelProperty(value = "件数")
    private int unit;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "品种")
    private String pdName;

    private String pcpName;

    private boolean single = true;

    private String updateTime;
    public Stock(){

    }

    public Stock(String id, String productId,
                 String principalId, String type,
                 int count, String standards,
                 double cost, double price,
                 String inDate, int unit, String remark,
                 String pdName, String pcpName,
                 boolean single, String updateTime) {
        this.id = id;
        this.productId = productId;
        this.principalId = principalId;
        this.type = type;
        this.count = count;
        this.standards = standards;
        this.cost = cost;
        this.price = price;
        this.inDate = inDate;
        this.unit = unit;
        this.remark = remark;
        this.pdName = pdName;
        this.pcpName = pcpName;
        this.single = single;
        this.updateTime = updateTime;
    }
}
