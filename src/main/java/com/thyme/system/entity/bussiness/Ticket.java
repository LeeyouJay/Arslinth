package com.thyme.system.entity.bussiness;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Ticket {
    private String id;

    private String principalId;

    @TableField(fill = FieldFill.INSERT)
    private String createTime;

    private String url;

    public Ticket() {

    }

    public Ticket(String id, String principalId, String createTime, String url) {
        this.id = id;
        this.principalId = principalId;
        this.createTime = createTime;
        this.url = url;
    }
}
