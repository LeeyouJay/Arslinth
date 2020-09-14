package com.thyme.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thyme.system.entity.bussiness.ProductImg;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author Arslinth
 * @Description TODO
 * @Date 2020/7/5
 */
@Repository
public interface ProductImgDao extends BaseMapper<ProductImg> {

    @Update("UPDATE product_img SET pd_name =#{currName} WHERE pd_name =#{preName}")
    int updatePdName(@Param("currName") String currName,@Param("preName")String preName);
}
