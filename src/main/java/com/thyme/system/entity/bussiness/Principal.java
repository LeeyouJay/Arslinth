package com.thyme.system.entity.bussiness;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;

/**
 * @author Arslinth
 * @ClassName Principal
 * @Description TODO
 * @Date 2020/6/16
 */
@Data
@Builder
public class Principal {

    private String id;

    private String pcpName;

    private String tel;

    private String address;

    private Date CreatTime;

    @JsonProperty(value = "PSBC")
    private String PSBC;//邮政银行卡

    @JsonProperty(value = "RCU")
    private String RCU;//信用卡

    public Principal(){

    }

    public Principal(String id, String pcpName, String tel, String address, Date creatTime, String PSBC, String RCU) {
        this.id = id;
        this.pcpName = pcpName;
        this.tel = tel;
        this.address = address;
        CreatTime = creatTime;
        this.PSBC = PSBC;
        this.RCU = RCU;
    }
}
