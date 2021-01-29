package com.thyme.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.thyme.system.dao.SaleDetailsDao;
import com.thyme.system.dao.SaleListDao;
import com.thyme.system.entity.bussiness.SaleDetails;
import com.thyme.system.entity.bussiness.SaleList;
import com.thyme.system.vo.SearchVO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SaleListService {

    private final SaleListDao saleListDao;

    private final SaleDetailsDao saleDetailsDao;

    public List<SaleList> getAllSales(int status,SearchVO searchVO){
        return saleListDao.getSales(status,searchVO.getSearchName(),searchVO.getStart(),searchVO.getEnd(),searchVO.getOther());
    }

    public List<SaleDetails> getSaleDetails(String id){
        QueryWrapper<SaleDetails> wrapper = new QueryWrapper<>();
        wrapper.eq("sale_id",id);
        return saleDetailsDao.selectList(wrapper);
    }
    public List<String> getAllSaleProduct() {
        return saleListDao.getAllSaleProduct();
    }

    public int deleteSales(List<String> ids) {
        return saleListDao.deleteBatchIds(ids);
    }

    public int recoverySale(String id) {
        return saleListDao.recoverySale(id);
    }
}
