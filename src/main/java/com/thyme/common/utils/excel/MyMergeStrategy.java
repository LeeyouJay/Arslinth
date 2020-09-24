package com.thyme.common.utils.excel;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Arslinth
 * @ClassName MyMergeStrategy
 * @Description TODO
 * @Date 2020/9/24
 */
public class MyMergeStrategy extends AbstractMergeStrategy {
    private Map<String, List<ExportExcelData>> collect;


    public MyMergeStrategy(Map<String, List<ExportExcelData>> collect) {
        this.collect = collect;
    }

    @Override
    protected void merge(Sheet sheet, Cell cell, Head head, Integer relativeRowIndex) {

        Integer count = 1;
        for (Map.Entry<String, List<ExportExcelData>> e : collect.entrySet()) {

            CellRangeAddress cellRangeAddress = new CellRangeAddress(count, count + e.getValue().size() - 1, 0, 0);
            if (e.getValue().size() == 1) {
                count += 1;
                continue;
            }
            sheet.addMergedRegionUnsafe(cellRangeAddress);
            count += e.getValue().size();
        }
    }
}
