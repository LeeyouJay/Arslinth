package com.thyme.common.utils.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;



/**
 * @author Arslinth
 * @ClassName ExcelData
 * @Description TODO
 * @Date 2020/6/21
 */
@Data
public class ExcelData {

    @ExcelProperty(value = "日期")
    @DateTimeFormat("yyyy-MM-dd")
    private String inDate;

    @ExcelProperty(value = "品种")
    private String pdName;

    @ExcelProperty(value = "数量(包)")
    private int count;

    @ExcelProperty(value = "单价")
    private double cost;

    @ExcelProperty(value = "零售价")
    private double price;


}
