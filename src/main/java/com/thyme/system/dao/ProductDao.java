package com.thyme.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thyme.common.utils.excel.ExportExcelData;
import com.thyme.system.entity.bussiness.Product;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author Arslinth
 * @Description TODO
 * @Date 2020/6/17
 */
@Repository
public interface ProductDao extends BaseMapper<Product> {
    @Select("SELECT  id,pd_name,type_id AS type ,price,period,yield,height,num,is_show,characters  FROM product")
    List<Product> getAllProducts();

    @Select("<script>" +
            "SELECT p.id,p.pd_name,p.price,p.cost,p.yield,p.period,p.height,p.characters,p.num,p.is_show,t.type_name AS type FROM product AS p LEFT JOIN type AS t ON t.id = p.type_id WHERE 1=1 " +
            "<when test=\" productName !='' and productName!=null\">" +
            "AND p.pd_name LIKE CONCAT('%',#{productName},'%') " +
            "</when>" +
            "ORDER BY create_time DESC,type_id" +
            "</script>")
    List<Product> getProducts(@Param("productName")String productName);

    @Select("SELECT * FROM product WHERE pd_name = #{pdName}")
    Product findProductByName(@Param("pdName") String pdName);

    @Select("SELECT id,pd_name,price,cost,yield,height,period,characters,num,type_id AS type FROM product WHERE id = #{id}")
    Product findProductById(@Param("id") String id);

    @Insert("<script>" +
            "INSERT INTO product(" +
            "<when test=\" type !='' and type!=null\">" +
            "type_id,"+
            "</when>" +
            "id,pd_name,price,cost,num,period,yield,height,characters,create_time) " +
            "VALUE(" +
            "<when test=\" type !='' and type!=null\">" +
            "#{type},"+
            "</when>" +
            "#{id},#{pdName},#{price},#{cost},#{num},#{period},#{yield},#{height},#{characters},NOW())" +
            "</script>")
    int addProduct(Product product);

    @Update("UPDATE product SET type_id=#{type}, pd_name=#{pdName},price=#{price},cost=#{cost},period=#{period},yield=#{yield},height=#{height},num=#{num},characters=#{characters} WHERE id = #{id}")
    int updateProduct(Product product);

    @Update("<script>" +
            "UPDATE product SET num = num+#{num} ,from_app = 0" +
            "<when test=\" price!= null and price!= 0\">" +
            ",price=#{price} " +
            "</when>" +
            "<when test=\" cost!= null and cost!= 0\">" +
            ",cost=#{cost} " +
            "</when>" +
            "WHERE pd_name = #{pdName}" +
            "</script>")
    int updateNumByName(Product product);


    @Delete("DELETE FROM product WHERE id = #{id}")
    int deleteProduct(@Param("id") String id);

    @Update("UPDATE product SET type_id=#{type} WHERE id=#{id}")
    int updateProductType(@Param("id") String id, @Param("type") String type);

    @Update("UPDATE product SET is_show = #{isShow} WHERE id = #{id}")
    int updateStatus(@Param("isShow") boolean isShow, @Param("id") String id);

    @Select("SELECT t.type_name,p.pd_name,p.period,p.yield,p.height,p.price  FROM product AS p LEFT JOIN type AS t ON p.type_id = t.id ORDER BY t.type_name ")
    List<ExportExcelData> forExport();
}
