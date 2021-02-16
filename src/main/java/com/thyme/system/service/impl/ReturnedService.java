package com.thyme.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.thyme.common.utils.MoneyUtils;
import com.thyme.common.utils.UUIDUtils;
import com.thyme.system.config.exception.RollbackException;
import com.thyme.system.dao.ProductDao;
import com.thyme.system.dao.RemittanceDao;
import com.thyme.system.dao.ReturnedDao;
import com.thyme.system.entity.bussiness.Product;
import com.thyme.system.entity.bussiness.Remittance;
import com.thyme.system.entity.bussiness.Returned;
import com.thyme.system.vo.SearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReturnedService {
    private final ReturnedDao returnedDao;

    private final ProductDao productDao;

    private final RemittanceDao remittanceDao;

    public List<Returned> getReturnedList(SearchVO searchVO){
        return returnedDao.getReturnedList(searchVO.getStart(),searchVO.getEnd(),searchVO.getSearchName(),searchVO.getOther(),searchVO.getOther1());
    }
    public List<Product> getProductsByPrincipalId(String pcpId){
        return productDao.getProductsByPrincipalId(pcpId);
    }

    public List<Returned> getReturnedListByPcpName(String pcpName){
        return returnedDao.getReturnedListByPcpName(pcpName);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int addReturned(Returned returned){
        returned.setId(UUIDUtils.getSixteenUUID());
        double totalCost = MoneyUtils.mul(returned.getCost(),returned.getCount());
        returned.setTotalCost(totalCost);
        try {
            Remittance remittance = new Remittance();
            remittance.setPrincipalId(returned.getPrincipalId());
            remittance.setDebt(totalCost);
            int i = remittanceDao.updateByPrincipalId(remittance);
            if(i == 0){
                throw new RollbackException("添加汇款记录异常！");
            }
        } catch (RollbackException e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return -1;
        }
        return returnedDao.insert(returned);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int deleteReturnedList(List<Returned> returnees){

        HashMap<String, List<Returned>> hashMap = returnees.stream().collect(Collectors.groupingBy(Returned::getPrincipalId, HashMap::new, Collectors.toList()));

        for (Map.Entry<String, List<Returned>> entry: hashMap.entrySet()){
            List<String> ids = entry.getValue().stream().map(Returned::getId).collect(Collectors.toList());
            QueryWrapper<Returned> wrapper = new QueryWrapper<>();
            wrapper.in("id",ids);
            List<Returned> returnedList = returnedDao.getReturnedListByIds(wrapper);
            double totalCost = returnedList.stream()
                    .map(returned -> MoneyUtils.mul(returned.getCount(), returned.getCost()))
                    .reduce(0.0, MoneyUtils::add);
            Remittance remittance = new Remittance();
            remittance.setPrincipalId(entry.getKey());
            remittance.setDebt(-totalCost);
            int i = remittanceDao.updateByPrincipalId(remittance);
            if(i == 0){
                try {
                    throw new RollbackException("添加汇款记录异常！");
                } catch (RollbackException e) {
                    e.printStackTrace();
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return -1;
                }
            }
            returnedDao.deleteBatchIds(ids);
        }
        return 1;
    }
}
