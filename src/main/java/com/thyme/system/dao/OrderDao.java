package com.thyme.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thyme.system.entity.bussiness.OrderList;
import com.thyme.system.vo.TotalValueVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import javax.lang.model.element.NestingKind;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Arslinth
 * @Description TODO
 * @Date 2020/7/11
 */
@Repository
public interface OrderDao extends BaseMapper<OrderList> {

    @Select("<script>" +
            "SELECT ol.* FROM order_list AS ol LEFT JOIN order_details AS od ON ol.id = od.order_id WHERE 1=1 " +
            "<choose>"+
            "<when test=\" start != '' and end != ''\"> " +
            "AND create_time BETWEEN #{start} AND #{end} " +
            "</when>" +
            "<when test=\" start != '' and end == ''\"> " +
            "<![CDATA[AND create_time >= #{start}]]> " +
            "</when>" +
            "<when test=\" start == '' and end != ''\"> " +
            "<![CDATA[AND create_time <= #{end}]]> " +
            "</when>" +
            "</choose>"+
            "<when test=\" consumer !='' and consumer!=null\">" +
            "AND consumer LIKE CONCAT('%',#{consumer},'%') " +
            "</when>" +
            "<when test=\" pdName !='' and pdName!=null\">" +
            "AND od.pd_name =#{pdName} " +
            "</when>" +
            "GROUP BY create_time " +
            "ORDER BY create_time DESC" +
            "</script>")
    List<OrderList> getOrders(@Param("consumer") String consumer,@Param("start") String start,@Param("end") String end,@Param("pdName") String pdName);

    @Select("SELECT pd_name FROM order_details GROUP BY pd_name")
    List<String> getAllOrderProduct();

    @Select("SELECT  IFNULL(SUM(total_price),0) AS totalPrice ,IFNULL(SUM(total_cost),0) AS totalCost " +
            "FROM order_list " +
            "WHERE  " +
            "is_deleted = 0 " +
            "AND PERIOD_DIFF( date_format( now( ) , '%Y%m' ) , date_format(create_time, '%Y%m' ) ) =1")
    Map<String,BigDecimal > preSalerooms();

    @Select("SELECT  IFNULL(SUM(total_price),0) AS totalPrice ,IFNULL(SUM(total_cost),0) AS totalCost " +
            "FROM order_list " +
            "WHERE " +
            "is_deleted = 0 " +
            "AND DATE_FORMAT(create_time, '%Y%m' ) = DATE_FORMAT(CURDATE(), '%Y%m')")
    Map<String,BigDecimal> curSalerooms();

    @Select("SELECT SUM(od.value) AS totalValue FROM order_details AS od " +
            "LEFT JOIN order_list AS ol ON od.order_id = ol.id " +
            "WHERE " +
            "ol.is_deleted = 0 " +
            "AND PERIOD_DIFF( date_format( now( ) , '%Y%m' ) , date_format(create_time, '%Y%m' ) ) =1")
    Double preValue();

    @Select("SELECT SUM(od.value) AS totalValue FROM order_details AS od " +
            "LEFT JOIN order_list AS ol ON od.order_id = ol.id " +
            "WHERE  " +
            "ol.is_deleted = 0 " +
            "AND DATE_FORMAT(create_time, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' )")
    Double curValue();

    @Select("SELECT IFNULL(SUM(total_price),0) AS totalPrice FROM order_list WHERE is_deleted = 0 " +
            "UNION ALL " +
            "SELECT IFNULL(SUM(total_price),0) AS totalPrice FROM order_list WHERE is_deleted = 0 AND YEAR(create_time)=YEAR(NOW())")
    List<Double> totalPrice();

    @Select("SELECT IFNULL(SUM(value),0) AS totalValue FROM order_details WHERE is_deleted = 0 " +
            "UNION ALL " +
            "SELECT IFNULL(SUM(od.value),0) AS totalValue FROM order_list AS ol " +
            "LEFT JOIN order_details AS od ON ol.id = od.order_id " +
            "WHERE ol.is_deleted = 0 AND YEAR(ol.create_time)=YEAR(NOW())")
    List<Integer> totalValue();

    @Select("SELECT IFNULL(SUM(total_cost),0) AS totalCost FROM order_list WHERE is_deleted = 0 " +
            "UNION ALL " +
            "SELECT IFNULL(SUM(total_cost),0) AS totalCost FROM order_list WHERE is_deleted = 0 AND YEAR(create_time)=YEAR(NOW())")
    List<Double> totalCost();

    @Select("SELECT IFNULL(SUM(count*cost),0) AS totalStock FROM stock " +
            "UNION ALL " +
            "SELECT IFNULL(SUM(count*cost),0) AS totalStock FROM stock WHERE YEAR(in_date)=YEAR(NOW())")
    List<Double> totalStock();


    @Select("SELECT MONTH(ol.create_time) as month,IFNULL(SUM(od.value),0) AS totalValue " +
            "FROM order_list AS ol " +
            "LEFT JOIN order_details AS od ON ol.id = od.order_id " +
            "WHERE ol.is_deleted = 0  " +
            "AND od.type =#{typeName} " +
            "AND YEAR(ol.create_time)=YEAR(NOW()) " +
            "GROUP BY MONTH(ol.create_time)")
    List<TotalValueVo> getTypeValue(@Param("typeName") String typeName);

    @Select("SELECT type FROM order_details WHERE is_deleted = 0 GROUP BY type")
    List<String> getDetailsTypeName();
}
