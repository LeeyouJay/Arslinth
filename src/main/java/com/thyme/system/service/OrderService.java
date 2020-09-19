package com.thyme.system.service;

import com.thyme.system.entity.bussiness.OrderDetails;
import com.thyme.system.entity.bussiness.OrderList;
import com.thyme.system.entity.bussiness.Region;
import com.thyme.system.vo.SearchVO;
import oshi.util.LsofUtil;

import java.util.List;
import java.util.Map;

/**
 * @author Arslinth
 * @Description TODO
 * @Date 2020/7/11
 */
public interface OrderService {

    List<OrderList> getOrders(int status, SearchVO searchVO);

    List<OrderList> getDelOrders(int status, SearchVO searchVO);

    List<String> getAllOrderProduct();

    int deleteOrders(List<String> ids);

    int recoveryOrder(String id);

    List<OrderDetails> getOrderDetails(String id);

    List<Region> getRegions();

    int addRegion(String district);

    int updateRegion(Region region);

    int deleteRegion(String id);

    Map<String ,Double[][]>  showGrowth();

    Map<String ,Object> showTotal();

    Map<String ,int[]> showTypeValue();
}
