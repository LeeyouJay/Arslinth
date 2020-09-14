package com.thyme.system.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thyme.system.entity.SysLog;
import com.thyme.system.entity.SysMenu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author thyme
 * @ClassName SysLogDao
 * @Description TODO
 * @Date 2020/1/9 16:22
 */
@Repository
public interface SysLogDao extends BaseMapper<SysLog> {

    @Select("<script>" +
            "SELECT * FROM sys_log WHERE username LIKE CONCAT('%',#{username},'%')" +
            "<choose>"+
                "<when test=\" start != '' and end != ''\"> " +
                    "AND create_date BETWEEN #{start} AND #{end}" +
                "</when>" +
                "<when test=\" start != '' and end == ''\"> " +
                    "<![CDATA[AND create_date > #{start}]]>" +
                "</when>" +
                "<when test=\" start == '' and end != ''\"> " +
                    "<![CDATA[AND create_date < #{end}]]>" +
                "</when>" +
                "<otherwise>" +
                    "AND 1 = 1" +
                "</otherwise>"+
            "</choose>"+
            " ORDER BY create_date desc"+
            "</script>")
    List<SysLog> findSysLogPage(@Param("start") String start, @Param("end")String end, @Param("username")String username);

}
