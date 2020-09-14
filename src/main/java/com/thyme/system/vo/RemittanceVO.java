package com.thyme.system.vo;

import com.thyme.system.entity.bussiness.Remittance;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * @author Arslinth
 * @ClassName RemittanceVO
 * @Description TODO
 * @Date 2020/6/19
 */
@Data
@SuperBuilder
public class RemittanceVO extends Remittance {

    private String pcpName;

    public RemittanceVO(){

    }

}
