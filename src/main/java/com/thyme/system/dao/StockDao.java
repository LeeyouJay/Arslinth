package com.thyme.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thyme.system.entity.bussiness.Principal;
import com.thyme.system.entity.bussiness.Stock;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Arslinth
 * @Description TODO
 * @Date 2020/6/11
 */
@Repository
public interface StockDao extends BaseMapper<Stock> {

    @Override
    int insert(Stock stock);



    @Update("UPDATE stock SET product_id=#{productId}, principal_id=#{principalId},count=#{count},cost=#{cost},price=#{price},in_date=#{inDate},unit=#{unit},remark=#{remark},update_time=NOW() WHERE id =#{id}")
    int updateStock(Stock stock);

    @Select("<script>" +
            "SELECT stock.*,prin.pcp_name,pd.pd_name " +
            "FROM stock AS stock " +
            "LEFT JOIN principal AS prin ON stock.principal_id = prin.id " +
            "LEFT JOIN product AS pd ON stock.product_id = pd.id " +
            "LEFT JOIN type AS t ON t.id = pd.type_id " +
            "WHERE 1=1" +
            "<choose>"+
            "<when test=\" start != '' and end != ''\"> " +
                "AND in_date BETWEEN #{start} AND #{end} " +
            "</when>" +
            "<when test=\" start != '' and end == ''\"> " +
                "<![CDATA[AND in_date >= #{start}]]> " +
            "</when>" +
            "<when test=\" start == '' and end != ''\"> " +
                "<![CDATA[AND in_date <= #{end}]]> " +
            "</when>" +
            "</choose>"+
            "<when test=\" principalId != null and principalId != ''\"> " +
                "AND stock.principal_id = #{principalId} " +
            "</when>" +
            "<when test=\" typeId != null and typeId != ''\"> " +
            "AND t.id=#{typeId} " +
            "</when>" +
            "<when test=\" pdName != null and pdName != ''\"> " +
            "AND pd.pd_name LIKE CONCAT('%',#{pdName},'%') " +
            "</when>" +
            "ORDER BY in_date DESC"+
            "</script>")
    List<Stock> getStocks(@Param("start") String start,
                          @Param("end")String end,
                          @Param("principalId")String principalId,
                          @Param("typeId") String typeId,
                          @Param("pdName") String pdName);

    @Select("<script>" +
            "SELECT stock.*,prin.pcp_name,pd.pd_name " +
            "FROM stock AS stock " +
            "LEFT JOIN principal AS prin ON stock.principal_id = prin.id " +
            "LEFT JOIN product AS pd ON stock.product_id = pd.id " +
            "WHERE stock.id IN " +
            "<foreach collection='ids' item='id' index='index' open='(' close=')' separator=','>" +
            "  #{id}" +
            "</foreach>" +
            "</script>")
    List<Stock> getStocksByIds(@Param("ids") List<String> ids);

    @Select("<script>" +
            "SELECT * FROM principal WHERE 1=1 " +
            "<when test=\" pcpName !='' and pcpName!=null\">" +
                "AND pcp_name LIKE CONCAT('%',#{pcpName},'%') " +
            "</when>" +
            "ORDER BY create_time DESC" +
            "</script>")
    List<Principal> getPrincipals(@Param("pcpName") String pcpName);

    @Insert("INSERT INTO principal (id,pcp_name,company,tel,address,PSBC,RCU,create_time) VALUES (#{id},#{pcpName},#{company},#{tel},#{address},#{PSBC},#{RCU},NOW())")
    int addPrincipal(Principal principal);

    @Update("UPDATE principal SET pcp_name = #{pcpName},company = #{company},tel=#{tel},address = #{address} ,PSBC=#{PSBC} WHERE id = #{id}")
    int updatePrincipal(Principal principal);

    @Delete("DELETE FROM principal WHERE id = #{id}")
    int deletePrincipal(@Param("id") String id);

    @Select("SELECT * FROM principal WHERE pcp_name = #{pcpName}")
    Principal getPrincipalByName(@Param("pcpName") String pcpName);

    @Select("SELECT * FROM principal WHERE id = #{id}")
    Principal getPrincipalById(@Param("id") String id);

    @Select("SELECT * FROM stock WHERE id=#{id}")
    Stock getStockById(@Param("id") String id);

    @Select("SELECT * FROM stock WHERE product_id = #{id}")
    List<Stock> findStockByProductId(@Param("id") String id);
}
