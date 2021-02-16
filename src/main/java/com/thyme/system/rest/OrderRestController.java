package com.thyme.system.rest;

import com.alibaba.fastjson.JSONObject;
import com.thyme.common.base.ApiResponse;
import com.thyme.system.entity.bussiness.*;
import com.thyme.system.service.OrderService;
import com.thyme.system.service.impl.SaleListService;
import com.thyme.system.vo.RemittanceVO;
import com.thyme.system.vo.SearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Arslinth
 * @ClassName OrderRestController
 * @Description TODO
 * @Date 2020/7/11
 */
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderRestController {

    private final OrderService orderService;

    private final SaleListService saleListService;

    @PostMapping("/getOrders")
    public ApiResponse getOrders(@RequestBody SearchVO searchVO) {
        JSONObject jsonObject = new JSONObject();
        List<OrderList> orders = orderService.getOrders(0, searchVO);
        jsonObject.put("total", orders.size());
        jsonObject.put("orderList", orders);
        return ApiResponse.ofSuccess(jsonObject);
    }

    @PostMapping("/getDelOrders")
    public ApiResponse getDelOrders(@RequestBody SearchVO searchVO) {
        JSONObject jsonObject = new JSONObject();
        List<OrderList> orders = orderService.getDelOrders(1, searchVO);
        jsonObject.put("total", orders.size());
        jsonObject.put("orderList", orders);
        return ApiResponse.ofSuccess(jsonObject);
    }
    @PostMapping("/getDelSales")
    public ApiResponse getDelSales(@RequestBody SearchVO searchVO) {
        JSONObject jsonObject = new JSONObject();
        List<SaleList> sales = saleListService.getAllSales(1, searchVO);
        jsonObject.put("total", sales.size());
        jsonObject.put("orderList", sales);
        return ApiResponse.ofSuccess(jsonObject);
    }

    @GetMapping("/getAllOrderProduct")
    public ApiResponse getAllOrderProduct() {
        JSONObject jsonObject = new JSONObject();
        List<String> saleProduct = saleListService.getAllSaleProduct();
        List<String> orderProduct = orderService.getAllOrderProduct();
        List<Object> list = Stream.of(saleProduct, orderProduct).flatMap(Collection::stream).distinct().collect(Collectors.toList());
        jsonObject.put("productList", list);
        return ApiResponse.ofSuccess(jsonObject);
    }

    @PostMapping("/deleteOrders")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    public ApiResponse deleteOrders(@RequestBody List<String> ids) {
        int i = orderService.deleteOrders(ids);
        if (i > 0) {
            return ApiResponse.success("删除成功！");
        } else
            return ApiResponse.fail("删除出现异常！");
    }

    @PostMapping("/deleteSales")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    public ApiResponse deleteSales(@RequestBody List<String> ids) {
        int i = saleListService.deleteSales(ids);
        if (i > 0) {
            return ApiResponse.success("删除成功！");
        } else
            return ApiResponse.fail("删除出现异常！");
    }
    @PostMapping("/changePayType")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    public ApiResponse changePayType(@RequestBody SaleList saleList) {
        int i = saleListService.changePayType(saleList);
        if (i > 0) {
            return ApiResponse.success("修改成功！");
        } else
            return ApiResponse.fail("修改出现异常！");
    }

    @GetMapping("/recoveryOrder")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    public ApiResponse recoveryOrder(@RequestParam("id") String id) {
        int i = orderService.recoveryOrder(id);
        if (i == 1)
            return ApiResponse.success("恢复成功！");
        else
            return ApiResponse.fail("恢复出现异常！");
    }

    @GetMapping("/recoverySale")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    public ApiResponse recoverySale(@RequestParam("id") String id) {
        int i = saleListService.recoverySale(id);
        if (i == 1)
            return ApiResponse.success("恢复成功！");
        else
            return ApiResponse.fail("恢复出现异常！");
    }

    @GetMapping("/getOrderDetails")
    public ApiResponse getOrderDetails(@RequestParam("id") String id) {
        JSONObject jsonObject = new JSONObject();
        List<OrderDetails> orderDetails = orderService.getOrderDetails(id);
        jsonObject.put("orderDetails", orderDetails);
        jsonObject.put("total", orderDetails.size());
        return ApiResponse.ofSuccess(jsonObject);
    }
    @GetMapping("/getSaleDetails")
    public ApiResponse getSaleDetails(@RequestParam("id") String id) {
        JSONObject jsonObject = new JSONObject();
        List<SaleDetails> saleDetails = saleListService.getSaleDetails(id);
        jsonObject.put("orderDetails", saleDetails);
        jsonObject.put("total", saleDetails.size());
        return ApiResponse.ofSuccess(jsonObject);
    }

    @GetMapping("/getDistrict")
    public ApiResponse getDistrict(){
        JSONObject jsonObject = new JSONObject();
        List<Region> regions = orderService.getRegions();
        jsonObject.put("regions",regions);
        return ApiResponse.ofSuccess(jsonObject);
    }

    @PostMapping("/addRegion")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    public ApiResponse addRegion(@RequestBody Region region){
        int i = orderService.addRegion(region.getDistrict());
        if ( i == 0)
            return ApiResponse.ofMessage(403,"存在相地区名称！");
        else if (i == 1)
            return ApiResponse.success("添加成功！");
        else
            return ApiResponse.fail("添加出现异常！");
    }

    @PostMapping("/updateRegion")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    public ApiResponse updateRegion(@RequestBody Region region){
        int i = orderService.updateRegion(region);
        if ( i == 0)
            return ApiResponse.ofMessage(403,"已存在相同名称！");
        else if (i == 1)
            return ApiResponse.success("字段更改为："+region.getDistrict());
        else
            return ApiResponse.fail("修改出现异常！");
    }

    @GetMapping("/deleteDistrict")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    public ApiResponse deleteType(@RequestParam("id") String id){
        int i = orderService.deleteRegion(id);
        if (i==0)
            return ApiResponse.ofMessage(403,"找不到对应id");
        else if (i==1)
            return ApiResponse.success("删除成功！");
        else
            return ApiResponse.fail("修改出现异常！");
    }

    @PostMapping("/getSales")
    public ApiResponse getSales(@RequestBody SearchVO searchVO) {
        JSONObject jsonObject = new JSONObject();
        List<SaleList> allSales = saleListService.getAllSales(0,searchVO);
        jsonObject.put("total", allSales.size());
        jsonObject.put("orderList", allSales);
        return ApiResponse.ofSuccess(jsonObject);
    }
}
