package com.thyme.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thyme.system.entity.bussiness.SaleList;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleListDao extends BaseMapper<SaleList> {


    @Select("<script>" +
            "SELECT ol.* FROM sale_list AS ol LEFT JOIN sale_details AS od ON ol.id = od.sale_id WHERE 1=1 " +
            "<choose>" +
            "<when test=\" start != '' and end != ''\"> " +
            "AND create_time BETWEEN #{start} AND #{end} " +
            "</when>" +
            "<when test=\" start != '' and end == ''\"> " +
            "<![CDATA[AND create_time >= #{start}]]> " +
            "</when>" +
            "<when test=\" start == '' and end != ''\"> " +
            "<![CDATA[AND create_time <= #{end}]]> " +
            "</when>" +
            "</choose>" +
            "<when test=\" consumer !='' and consumer!=null\">" +
            "AND consumer LIKE CONCAT('%',#{consumer},'%') " +
            "</when>" +
            "<when test=\" pdName !='' and pdName!=null\">" +
            "AND od.pd_name =#{pdName} " +
            "</when>" +
            "<when test=\" status == 0\">" +
            "AND ol.is_deleted = 0 " +
            "</when>" +
            "<when test=\" status == 1\">" +
            "AND ol.is_deleted = 1 " +
            "</when>" +
            "GROUP BY create_time " +
            "ORDER BY create_time DESC" +
            "</script>")
    List<SaleList> getSales(@Param("status") int status, @Param("consumer") String consumer, @Param("start") String start, @Param("end") String end, @Param("pdName") String pdName);

    @Select("SELECT pd_name FROM sale_details GROUP BY pd_name")
    List<String> getAllSaleProduct();

    @Update("UPDATE sale_list SET is_deleted = 0 WHERE id = #{id}")
    int recoverySale(@Param("id") String id);
}
