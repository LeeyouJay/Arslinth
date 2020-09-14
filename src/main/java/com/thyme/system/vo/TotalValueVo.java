package com.thyme.system.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author Arslinth
 * @ClassName totalValueVo
 * @Description TODO
 * @Date 2020/7/20
 */
@Data
@Builder
public class TotalValueVo {
    private int month;

    private int totalValue;

    public TotalValueVo() {
    }

    public TotalValueVo(int month, int totalValue) {
        this.month = month;
        this.totalValue = totalValue;
    }
}
