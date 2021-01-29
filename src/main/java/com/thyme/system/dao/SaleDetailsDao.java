package com.thyme.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thyme.system.entity.bussiness.SaleDetails;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleDetailsDao extends BaseMapper<SaleDetails> {
}
