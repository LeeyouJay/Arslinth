package com.thyme.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thyme.system.entity.bussiness.Product;
import com.thyme.system.entity.bussiness.Type;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Arslinth
 * @Description TODO
 * @Date 2020/7/4
 */
@Repository
public interface TypeDao extends BaseMapper<Type> {

    @Select("SELECT * FROM product WHERE type_id = #{id}")
    List<Product> checkStockByType(@Param("id") String id);
}
