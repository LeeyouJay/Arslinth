package com.thyme.system.rest;

import com.alibaba.fastjson.JSONObject;
import com.thyme.common.base.ApiResponse;
import com.thyme.system.entity.bussiness.OrderDetails;
import com.thyme.system.entity.bussiness.OrderList;
import com.thyme.system.entity.bussiness.Region;
import com.thyme.system.service.OrderService;
import com.thyme.system.vo.RemittanceVO;
import com.thyme.system.vo.SearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/getOrders")
    public ApiResponse getPrincipal(@RequestBody SearchVO searchVO) {
        JSONObject jsonObject = new JSONObject();
        List<OrderList> orders = orderService.getOrders(searchVO);
        jsonObject.put("total", orders.size());
        jsonObject.put("orderList", orders);
        return ApiResponse.ofSuccess(jsonObject);
    }

    @GetMapping("/getAllOrderProduct")
    public ApiResponse getAllOrderProduct(){
        JSONObject jsonObject = new JSONObject();
        List<String> list = orderService.getAllOrderProduct();
        jsonObject.put("productList", list);
        return ApiResponse.ofSuccess(jsonObject);
    }

    @GetMapping("/getOrderDetails")
    public ApiResponse getOrderDetails(@RequestParam("id") String id){
        JSONObject jsonObject = new JSONObject();
        List<OrderDetails> orderDetails = orderService.getOrderDetails(id);
        jsonObject.put("orderDetails",orderDetails);
        jsonObject.put("total",orderDetails.size());
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
}
