package com.thyme.system.vo;

import com.thyme.system.entity.bussiness.Stock;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * @author Arslinth
 * @ClassName StockVO
 * @Description TODO
 * @Date 2020/6/12
 */
@Data
@SuperBuilder
public class StockVO extends Stock {

    private double totalCost;//总成本价

    private double totalPrice;//总销售价

    private double earning;//利润

    public StockVO(){

    }

}
