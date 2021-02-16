package com.thyme.system.dao;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.thyme.system.entity.bussiness.Returned;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnedDao extends BaseMapper<Returned> {
    @Select("<script>" +
            "SELECT t.*,p.pd_name,pr.pcp_name FROM returned AS t  " +
            "LEFT JOIN principal AS pr ON t.principal_id = pr.id  " +
            "LEFT JOIN product AS p ON t.product_id = p.id " +
            "WHERE 1=1 " +
            "<choose>"+
            "<when test=\" start != '' and end != ''\"> " +
            "AND t.create_time BETWEEN #{start} AND #{end} " +
            "</when>" +
            "<when test=\" start != '' and end == ''\"> " +
            "<![CDATA[AND t.create_time >= #{start}]]> " +
            "</when>" +
            "<when test=\" start == '' and end != ''\"> " +
            "<![CDATA[AND t.create_time <= #{end}]]> " +
            "</when>" +
            "</choose>" +
            "<when test=\" principalId != null and principalId != ''\"> " +
            "AND t.principal_id = #{principalId} " +
            "</when>" +
            "<when test=\" typeId != null and typeId != ''\"> " +
            "AND p.type_id=#{typeId} " +
            "</when>" +
            "<when test=\" pdName != null and pdName != ''\"> " +
            "AND p.pd_name LIKE CONCAT('%',#{pdName},'%') " +
            "</when>" +
            "ORDER BY create_time DESC"+
            "</script>")
    List<Returned> getReturnedList(@Param("start") String start,
                                   @Param("end")String end,
                                   @Param("principalId")String principalId,
                                   @Param("typeId") String typeId,
                                   @Param("pdName") String pdName);
    @Select("SELECT * FROM returned ${ew.customSqlSegment}")
    List<Returned> getReturnedListByIds(@Param(Constants.WRAPPER) Wrapper<Returned> wrapper);

    @Select("<script>" +
            "SELECT r.*,p.pcp_name FROM returned AS r LEFT JOIN principal AS p ON r.principal_id = p.id WHERE 1=1 " +
            "<when test=\" pcpName !='' and pcpName!=null\">" +
            "AND p.pcp_name LIKE CONCAT('%',#{pcpName},'%') " +
            "</when> " +
            "</script>" )
    List<Returned> getReturnedListByPcpName( @Param("pcpName")String pcpName);
}
