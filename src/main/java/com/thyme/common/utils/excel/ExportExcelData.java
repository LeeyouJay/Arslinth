package com.thyme.common.utils.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * @author Arslinth
 * @ClassName ExportExcelData
 * @Description TODO
 * @Date 2020/9/24
 */
@Data
public class ExportExcelData {

    @ExcelProperty(value = "类型")
    private String typeName;

    @ColumnWidth(20)//列宽
    @ExcelProperty(value = "品种")
    private String pdName;

    @ExcelProperty(value = "生育期(天)")
    private int period;

    @ExcelProperty(value = "亩产量(公斤)")
    private double yield;

    @ExcelProperty(value = "株高(cm)")
    private double height;

    @ExcelProperty(value = "零售价(元/包)")
    private double price;


}
