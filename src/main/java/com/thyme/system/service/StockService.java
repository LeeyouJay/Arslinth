package com.thyme.system.service;

import com.thyme.system.entity.bussiness.Principal;
import com.thyme.system.entity.bussiness.Stock;
import com.thyme.system.vo.SearchVO;
import com.thyme.system.vo.StockVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author Arslinth
 * @Description TODO
 * @Date 2020/6/12
 */
public interface StockService {

    List<StockVO> getStocks(SearchVO searchVO);

    int addStock(Stock stock);

    int updateStock(Stock stock);

    int deleteStocksByIds(List<Map<String,Object>> checkList);

    List<Principal> getPrincipals(String pcpName);

    Principal getPrincipalById(String id);

    Stock getStockById(String id);

    int addPrincipal(Principal principal);

    int updatePrincipal(Principal principal);

    int deletePrincipal(String id);

}
