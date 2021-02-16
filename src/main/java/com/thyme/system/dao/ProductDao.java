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
    @Select("SELECT  id,pd_name,type_id AS type ,price,unit,period_min,period_max,yield,height,num,is_show,characters  FROM product")
    List<Product> getAllProducts();

    @Select("<script>" +
            "SELECT p.id,p.pd_name,p.price,p.cost,p.yield,p.period_min,p.unit,p.period_max,p.height,p.characters,p.num,p.is_show,p.num_unit,t.type_name AS type FROM product AS p LEFT JOIN type AS t ON t.id = p.type_id WHERE 1=1 " +
            "<when test=\" productName !='' and productName!=null\">" +
            "AND p.pd_name LIKE CONCAT('%',#{productName},'%') " +
            "</when>" +
            "<when test=\" typeId !='' and typeId!=null\">" +
            "AND p.type_id =#{typeId} " +
            "</when>" +
            "ORDER BY type_id ,create_time DESC" +
            "</script>")
    List<Product> getProducts(@Param("productName")String productName,@Param("typeId")String typeId);

    @Select("SELECT * FROM product WHERE pd_name = #{pdName}")
    Product findProductByName(@Param("pdName") String pdName);

    @Select("SELECT id,pd_name,price,cost,yield,height,unit,period_min,period_max,characters,num,type_id AS type FROM product WHERE id = #{id}")
    Product findProductById(@Param("id") String id);

    @Insert("<script>" +
            "INSERT INTO product(" +
            "<when test=\" type !='' and type!=null\">" +
            "type_id," +
            "</when>" +
            "id,pd_name,price,cost,num,period_min,period_max,unit,yield,height,characters,num_unit,create_time) " +
            "VALUE(" +
            "<when test=\" type !='' and type!=null\">" +
            "#{type}," +
            "</when>" +
            "#{id},#{pdName},#{price},#{cost},#{num},#{periodMin},#{periodMax},#{unit},#{yield},#{height},#{characters},#{numUnit},NOW())" +
            "</script>")
    int addProduct(Product product);

    @Update("UPDATE product SET type_id=#{type}, pd_name=#{pdName},price=#{price},cost=#{cost},period_min=#{periodMin},period_max=#{periodMax},unit=#{unit},yield=#{yield},height=#{height},num=#{num},characters=#{characters} WHERE id = #{id}")
    int updateProduct(Product product);

    @Update("<script>" +
            "UPDATE product SET num = num+#{num} ,from_app = 0" +
            "<when test=\" price!= null and price!= 0\">" +
            ",price=#{price} " +
            "</when>" +
            "<when test=\" cost!= null and cost!= 0\">" +
            ",cost=#{cost} " +
            "</when>" +
            "<when test=\" numUnit != 1 and numUnit != 0\">" +
            ",num_unit=#{numUnit} " +
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

    @Update("UPDATE product SET unit = #{unit} ,num_unit = #{numUnit} WHERE id = #{id}")
    int updateUnit(@Param("unit") String unit,@Param("numUnit") String numUnit, @Param("id") String id);

    @Select("SELECT t.type_name AS type,p.pd_name,p.period_min,p.period_max,p.unit,p.yield,p.height,p.price  FROM product AS p LEFT JOIN type AS t ON p.type_id = t.id ORDER BY t.type_name ")
    List<Product> forExport();

    @Select("SELECT p.* FROM stock AS s LEFT JOIN product AS p ON s.product_id = p.id\n" +
            "WHERE s.principal_id = #{id}")
    List<Product> getProductsByPrincipalId(@Param("id")String pcpId);
}
