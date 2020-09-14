package com.thyme.system.entity.bussiness;

import lombok.Data;

/**
 * @author Arslinth
 * @ClassName District
 * @Description TODO
 * @Date 2020/7/21
 */
@Data
public class Region {
    private int id;

    private String district;

    public Region() {
    }

    public Region(String district) {
        this.district = district;
    }
}
