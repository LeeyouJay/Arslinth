package com.thyme.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thyme.system.entity.bussiness.Remittance;
import com.thyme.system.vo.RemittanceVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Arslinth
 * @Description TODO
 * @Date 2020/6/19
 */
@Repository
public interface RemittanceDao extends BaseMapper<Remittance> {

    @Update("UPDATE remittance SET total_cost=total_cost+#{totalCost}, total_count=total_count+#{totalCount}, debt=debt+#{debt} WHERE principal_id = #{principalId}")
    int updateByPrincipalId(Remittance remittance);

    @Update("UPDATE remittance SET debt=#{debt},total_pay=#{totalPay} WHERE principal_id = #{principalId}")
    int updateDebt(Remittance remittance);

    @Select("<script>" +
            "SELECT r.*,p.pcp_name FROM remittance AS r LEFT JOIN principal AS p ON r.principal_id = p.id WHERE 1=1 " +
            "<when test=\" pcpName !='' and pcpName!=null\">" +
            "AND p.pcp_name LIKE CONCAT('%',#{pcpName},'%') " +
            "</when>" +
            "ORDER BY r.total_cost DESC " +
            "</script>")
    List<RemittanceVO> getRemittances(@Param("pcpName") String pcpName);
}
