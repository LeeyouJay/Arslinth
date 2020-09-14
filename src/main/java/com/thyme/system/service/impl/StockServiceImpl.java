package com.thyme.system.service.impl;

import com.alibaba.excel.EasyExcel;
import com.thyme.common.base.ApiResponse;
import com.thyme.common.utils.MoneyUtils;
import com.thyme.common.utils.UUIDUtils;
import com.thyme.common.utils.excel.ExcelListener;
import com.thyme.system.config.exception.RollbackException;
import com.thyme.system.dao.ProductDao;
import com.thyme.system.dao.RemittanceDao;
import com.thyme.system.dao.StockDao;
import com.thyme.system.entity.bussiness.*;
import com.thyme.system.service.PayRecordService;
import com.thyme.system.service.RemittanceService;
import com.thyme.system.service.StockService;
import com.thyme.system.vo.SearchVO;
import com.thyme.system.vo.StockVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Arslinth
 * @ClassName StockServiceImpl
 * @Description TODO
 * @Date 2020/6/12
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StockServiceImpl implements StockService {

    private final StockDao stockDao;

    private final RemittanceService remittanceService;

    private final ProductDao productDao;

    @Override
    public List<StockVO> getStocks(SearchVO searchVO) {
        List<Stock> stocks = stockDao.getStocks(searchVO.getStart(),searchVO.getEnd(),searchVO.getSearchName(),searchVO.getOther(),searchVO.getOther1());
        List<StockVO> stockVOS = new ArrayList<>();
        for (Stock stock:stocks){
            double totalPrice = MoneyUtils.mul(stock.getPrice(),stock.getCount());
            double totalCost = MoneyUtils.mul(stock.getCost(),stock.getCount());
            stockVOS.add(StockVO.builder()
                    .id(stock.getId())
                    .principalId(stock.getPrincipalId())
                    .productId(stock.getProductId())
                    .pdName(stock.getPdName())
                    .pcpName(stock.getPcpName())
                    .inDate(stock.getInDate())
                    .unit(stock.getUnit())
                    .count(stock.getCount())
                    .price(stock.getPrice())
                    .remark(stock.getRemark())
                    .cost(stock.getCost())
                    .updateTime(stock.getUpdateTime())
            .totalPrice(totalPrice)
            .totalCost(totalCost)
            .earning(MoneyUtils.sub(totalPrice,totalCost)).build());
        }

        return stockVOS;
    }

    @Override
    public Stock getStockById(String id) {
        return stockDao.getStockById(id);
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int addStock(Stock stock) {

        try {
            Stock buildStock = Stock.builder()
                    .id(UUIDUtils.getUUID())
                    .cost(stock.getCost())
                    .count(stock.getCount())
                    .inDate(stock.getInDate())
                    .price(stock.getPrice())
                    .principalId(stock.getPrincipalId())
                    .productId(stock.getProductId())
                    .unit(stock.getUnit())
                    .remark(stock.getRemark()).build();

            if(stock.isSingle()) {
                int i = productDao.updateNumByName(Product.builder()
                        .pdName(stock.getPdName())
                        .num(stock.getCount())
                        .cost(stock.getCost())
                        .price(stock.getPrice())
                        .build());
                if(i == 0){
                    throw new RollbackException("增加库存数据异常！");
                }
            }


            double totalCost = MoneyUtils.mul(stock.getCost(),stock.getCount());

            Remittance remittance = new Remittance();

            remittance.setPrincipalId(stock.getPrincipalId());
            remittance.setTotalCount(stock.getCount());
            remittance.setTotalCost(MoneyUtils.add(0,totalCost));
            remittance.setDebt(MoneyUtils.sub(0,remittance.getTotalCost()));

            int i = remittanceService.updateByPrincipalId(remittance);
            if(i == 0){
                throw new RollbackException("添加汇款记录异常！");
            }

            return stockDao.insert(buildStock);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return -1;
        }
    }

    @Override
    @Transactional()
    public int updateStock(Stock stock) {
        try {
            Stock stockById = stockDao.getStockById(stock.getId());
            int i = productDao.updateNumByName(Product.builder()
                    .pdName(stock.getPdName())
                    .num(stock.getCount()-stockById.getCount())
                    .price(stock.getPrice())
                    .cost(stock.getCost())
                    .build());
            if (i == 0){
                throw new RollbackException("品种信息更新错误");
            }
            Remittance remittance = remittanceService.findByPrincipalId(stock.getPrincipalId());
            if (remittance == null){
                throw new RollbackException("找不到对应的汇款记录");
            }

            double preTC= MoneyUtils.mul(stockById.getCost(), stockById.getCount());//原先总价
            double TC = MoneyUtils.mul(stock.getCost(), stock.getCount());//更改后总价

            double totalCost = MoneyUtils.sub(TC, preTC);
            double debt = -totalCost;

            remittance.setTotalCost(totalCost);
            remittance.setDebt(debt);
            remittance.setTotalCount(stock.getCount()-stockById.getCount());
            int row = remittanceService.updateByPrincipalId(remittance);
            if(row != 1){
                throw new RollbackException("更新汇款记录失败！");
            }
            return stockDao.updateStock(stock);
        } catch (RollbackException e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return -1;
        }
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int deleteStocksByIds(List<Map<String,Object>> checkList) {
        try {
            int status = 0;
            for (Map<String, Object> stringObjectMap : checkList) {
                String principalId = stringObjectMap.get("principalId").toString();
                List<String> ids = (List<String>) stringObjectMap.get("ids");
                Remittance remittance = remittanceService.findByPrincipalId(principalId);
                List<Stock> stocks = stockDao.getStocksByIds(ids);
                if (remittance == null) {
                    throw new RollbackException("找不到对应的汇款记录");
                }

                double totalCost = 0;
                double debt = 0;
                int totalCount = 0;
                for (Stock s : stocks) {
                    totalCost = MoneyUtils.sub(totalCost, MoneyUtils.mul(s.getCost(), s.getCount()));
                    debt = MoneyUtils.add(debt, MoneyUtils.mul(s.getCost(), s.getCount()));
                    totalCount = totalCount - s.getCount();
                    if (productDao.updateNumByName(Product.builder()
                            .pdName(s.getPdName())
                            .num(-s.getCount())
                            .build()) == 0)
                        throw new RollbackException("更新库存失败，找不到对应品种");
                }
                remittance.setTotalCost(totalCost);
                remittance.setDebt(debt);
                remittance.setTotalCount(totalCount);
                int row = remittanceService.updateByPrincipalId(remittance);
                if (row != 1) {
                    throw new RollbackException("更新汇款记录失败！");
                }
                status = stockDao.deleteBatchIds(ids);
            }
            if (status>0)
                return 1;
            else
                return 0;

        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return -1;
        }
    }

    @Override
    public List<Principal> getPrincipals(String pcpName) {
        return stockDao.getPrincipals(pcpName);
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int addPrincipal(Principal principal) {

        try {
            Principal p = stockDao.getPrincipalByName(principal.getPcpName());
            if (p != null){
                return 0;
            }
            Principal buildPrincipal = Principal.builder()
                    .id(UUIDUtils.getUUID())
                    .pcpName(principal.getPcpName())
                    .address(principal.getAddress())
                    .tel(principal.getTel())
                    .PSBC(principal.getPSBC())
                    .RCU(principal.getRCU()).build();

            remittanceService.addRemittance(Remittance.builder()
                    .id(UUIDUtils.getUUID())
                    .principalId(buildPrincipal.getId()).build());

            return stockDao.addPrincipal(buildPrincipal);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return -1;
        }

    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int updatePrincipal(Principal principal) {

        try {
            Principal principalByName = stockDao.getPrincipalByName(principal.getPcpName());
            if(principalByName != null && !principalByName.getId().equals(principal.getId())){
                return 0;
            }else{
                stockDao.updatePrincipal(principal);
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return -1;
        }
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int deletePrincipal(String id) {

        try {
            List<Stock> stocks = stockDao.getStocks("", "", id,"","");
            Remittance remittance = remittanceService.findByPrincipalId(id);

            if(stocks.size()>0 || (remittance != null && remittance.getDebt() < 0)){
                return 0;
            }else {
                stockDao.deletePrincipal(id);
                remittanceService.deleteByPrincipalId(id);
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return -1;
        }

    }

    @Override
    public Principal getPrincipalById(String id) {
        return stockDao.getPrincipalById(id);
    }

}
