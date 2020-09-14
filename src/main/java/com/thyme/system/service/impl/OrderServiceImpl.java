package com.thyme.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.thyme.common.utils.MoneyUtils;
import com.thyme.system.dao.OrderDao;
import com.thyme.system.dao.OrderDetailsDao;
import com.thyme.system.dao.RegionDao;
import com.thyme.system.dao.StockDao;
import com.thyme.system.entity.bussiness.*;
import com.thyme.system.service.OrderService;
import com.thyme.system.vo.SearchVO;
import com.thyme.system.vo.TotalValueVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Arslinth
 * @ClassName OrderServiceImpl
 * @Description TODO
 * @Date 2020/7/11
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;

    private final OrderDetailsDao orderDetailsDao;

    private final RegionDao regionDao;
    @Override
    public List<OrderList> getOrders(SearchVO searchVO) {
        return orderDao.getOrders(searchVO.getSearchName(),searchVO.getStart(),searchVO.getEnd(),searchVO.getOther());
    }

    @Override
    public List<String> getAllOrderProduct() {
        return orderDao.getAllOrderProduct();
    }

    @Override
    public List<OrderDetails> getOrderDetails(String id) {
        QueryWrapper<OrderDetails> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id",id);
        return orderDetailsDao.selectList(wrapper);
    }

    @Override
    public List<Region> getRegions() {
        return regionDao.selectList(new QueryWrapper<Region>());
    }

    @Override
    public int addRegion(String district) {
        QueryWrapper<Region> query = new QueryWrapper<>();
        query.eq("district",district);
        Region one = regionDao.selectOne(query);
        if(one != null)
            return 0;
        return regionDao.insert(new Region(district));
    }

    @Override
    public int updateRegion(Region region) {
        QueryWrapper<Region> query = new QueryWrapper<>();
        query.eq("district",region.getDistrict());
        Region one = regionDao.selectOne(query);
        if(one != null)
            return 0;
        return regionDao.updateById(region);
    }

    @Override
    public int deleteRegion(String id) {
        return regionDao.deleteById(id);
    }

    @Override
    public Map<String ,Double[][]> showGrowth() {
        Map<String, BigDecimal> preMap = orderDao.preSalerooms();
        Map<String, BigDecimal> curMap = orderDao.curSalerooms();
        HashMap<String, Double[][]> growth = new HashMap<>();

        BigDecimal prePrice = preMap.get("totalPrice");
        double preEarns = prePrice.subtract(preMap.get("totalCost")).doubleValue();
        Double preValue = orderDao.preValue();
        preValue = preValue==null?0:preValue;

        Double[][] p ={{prePrice.doubleValue()},{preValue},{preEarns}};
        growth.put("pre",p);

        BigDecimal curPrice = curMap.get("totalPrice");
        double curEarns = curPrice.subtract(curMap.get("totalCost")).doubleValue();
        Double curValue = orderDao.curValue();
        curValue = curValue==null?0:curValue;
        Double[][] c ={{curPrice.doubleValue()},{curValue},{curEarns}};
        growth.put("cur",c);

        return growth;
    }

    @Override
    public Map<String, Object> showTotal() {
        Map<String, Object> map = new HashMap<>();
        List<Double> totalPrice = orderDao.totalPrice();
        List<Double> totalCost = orderDao.totalCost();
        List<Integer> totalValue = orderDao.totalValue();
        List<Double> totalStock = orderDao.totalStock();

        List<Double> totalEarns = new ArrayList<>();
        totalEarns.add(MoneyUtils.sub(totalPrice.get(0),totalCost.get(0)));
        totalEarns.add(MoneyUtils.sub(totalPrice.get(1),totalCost.get(1)));

        map.put("totalValue",totalValue);
        map.put("totalPrice",totalPrice);
        map.put("totalEarns",totalEarns);
        map.put("totalStock",totalStock);
        return map;
    }

    @Override
    public Map<String, int[]> showTypeValue() {
        List<String> typeName = orderDao.getDetailsTypeName();
        Map<String, int[]> map = new HashMap<>();
        for (String n:typeName){
            List<TotalValueVo> typeValue = orderDao.getTypeValue(n);
            int[] ints = new int[12];
            for (TotalValueVo v:typeValue){
                ints[v.getMonth()-1] = v.getTotalValue();
            }
            map.put(n,ints);
        }
        return map;
    }
}
