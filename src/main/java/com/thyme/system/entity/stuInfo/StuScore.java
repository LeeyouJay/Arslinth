package com.thyme.system.entity.stuInfo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;


/**
 * @author Arslinth
 * @ClassName StuScore
 * @Description TODO
 * @Date 2020/5/19 11:24
 */
@Data
@Builder
public class StuScore implements Serializable {

    private String id;

    private String stuId;

    private String name;

    private double chinese;

    private double math;

    private double english;

    private double total;

    private int classNum;

    @TableField(fill = FieldFill.INSERT)
    private String createTime;

    @TableField(fill = FieldFill.UPDATE)
    private String updateTime;

    public StuScore() {
    }

    public StuScore(String id, String stuId, String name, double chinese, double math, double english, double total, int classNum, String createTime, String updateTime) {
        this.id = id;
        this.stuId = stuId;
        this.name = name;
        this.chinese = chinese;
        this.math = math;
        this.english = english;
        this.total = total;
        this.classNum = classNum;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}
