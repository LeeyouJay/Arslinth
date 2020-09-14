package com.thyme.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thyme.system.entity.bussiness.OrderDetails;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author Arslinth
 * @Description TODO
 * @Date 2020/7/11
 */
@Repository
public interface OrderDetailsDao extends BaseMapper<OrderDetails> {
}
