package com.thyme.system.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author thyme
 * @ClassName SysUser
 * @Description TODO
 * @Date 2019/12/12 21:44
 */
@Data
@Builder
public class SysUser implements Serializable {

    static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private String password;

    private String nickName;

    private String avatar;

    private String sex;

    private String mobile;

    private String email;

    private String birthday;

    private String hobby;

    private String liveAddress;

    private String createTime;

    private String updateTime;

    public SysUser(){

    }

    public SysUser(String id, String name, String password, String nickName, String avatar, String sex, String mobile, String email, String birthday, String hobby, String liveAddress, String createTime, String updateTime) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.nickName = nickName;
        this.avatar = avatar;
        this.sex = sex;
        this.mobile = mobile;
        this.email = email;
        this.birthday = birthday;
        this.hobby = hobby;
        this.liveAddress = liveAddress;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}
