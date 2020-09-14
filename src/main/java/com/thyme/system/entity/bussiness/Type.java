package com.thyme.system.entity.bussiness;

import lombok.Builder;
import lombok.Data;

/**
 * @author Arslinth
 * @ClassName Type
 * @Description TODO
 * @Date 2020/7/4
 */
@Data
@Builder
public class Type {
    private String id;

    private String typeName;

    public Type() {
    }

    public Type(String id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }
}
