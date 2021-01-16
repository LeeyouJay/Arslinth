package com.thyme.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thyme.system.entity.bussiness.Ticket;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketDao extends BaseMapper<Ticket> {

    @Select("SELECT * FROM ticket WHERE principal_id = #{principalId} ORDER BY create_time DESC ")
    List<Ticket> getTicketsByPcpId(@Param("principalId") String principalId);

}
