package com.thyme;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;

import com.thyme.common.base.Constants;
import com.thyme.common.utils.FileUtils;
import com.thyme.system.dao.*;
import com.thyme.system.entity.SysUser;
import com.thyme.system.entity.bussiness.Region;
import com.thyme.system.service.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author thyme
 * @ClassName MybatisTest
 * @Description TODO
 * @Date 2019/12/10 21:58
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MybatisTest {

    @Autowired
    private SysUserDao userDao;


    @Test
    public void insert() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        userDao.insert(
                SysUser.builder()
                .id(uuid)
                .name("admin")
                .password("123")
                .createTime(new Date())
                .build()
        );
    }

    @Test
    public void password() {
        String encode = new BCryptPasswordEncoder().encode("123");
        System.out.println(encode);
    }


    @Test
    public void testInvoicev1() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json; charset=utf-8");
        headers.put("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJCREE0RkRXeDQ5cWFyaTlyY1hsMDh6dWJrSzFORkxydiJ9.KH5Afj0s95NHDBAXiGxCKUNn1AT7z7NJuKc-xjQo2Ak");
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("image", FileUtil.file("E:\\1.jpg"));
        String url = "http://ai.aisino.com:28000/invoicev1";
        String body = HttpRequest.post(url)
                .form(paramMap)
                .addHeaders(headers)
                .execute().body();
        JSONObject jsonObject = JSONObject.parseObject(body);
        System.out.println(jsonObject);
    }

    @Test
    public void testIp() throws Exception{
        DbConfig config = new DbConfig();
        String path = "config/ip2region.db";
        String name = "ip2region.db";
        File file = FileUtils.inputStreamToFile(new ClassPathResource(path).getStream(), name);
        DbSearcher searcher = new DbSearcher(config, file.getPath());
        DataBlock dataBlock = searcher.btreeSearch("127.0.0.1");
        String address = dataBlock.getRegion().replace("0|","");
        if(address.charAt(address.length()-1) == '|'){
            address = address.substring(0,address.length() - 1);
        }
        String s = address.equals(Constants.REGION) ? "内网IP" : address;
        System.out.println(s);
    }


}
