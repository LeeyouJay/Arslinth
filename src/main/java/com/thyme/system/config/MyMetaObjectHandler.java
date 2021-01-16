package com.thyme.system.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Arslinth
 * @ClassName MyMetaObjectHandler
 * @Description TODO
 * @Date 2020/8/22
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void insertFill(MetaObject metaObject) {
        String date = format.format(new Date());
        this.setFieldValByName("createTime" ,date,metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        String date = format.format(new Date());
        this.setFieldValByName("updateTime" ,date,metaObject);
    }
}
