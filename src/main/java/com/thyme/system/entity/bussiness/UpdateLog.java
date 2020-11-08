package com.thyme.system.entity.bussiness;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Date;


/**
 * @author Arslinth
 * @ClassName UpdateLog
 * @Description TODO
 * @Date 2020/11/10
 */
@Data
public class UpdateLog {

    @TableId(type = IdType.AUTO)
    private String id;

    private String time;

    private String title;

    private String context;

    public UpdateLog() {
    }

    public UpdateLog(String id, String time, String title, String context) {
        this.id = id;
        this.time = time;
        this.title = title;
        this.context = context;
    }
}
