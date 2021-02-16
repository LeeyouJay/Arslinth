package com.thyme.system.rest;

import com.alibaba.fastjson.JSONObject;
import com.thyme.common.base.ApiResponse;
import com.thyme.system.entity.bussiness.Product;
import com.thyme.system.entity.bussiness.Returned;
import com.thyme.system.service.ProductService;
import com.thyme.system.service.impl.ReturnedService;
import com.thyme.system.vo.RemittanceVO;
import com.thyme.system.vo.SearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/returned")
@RestController
@RequiredArgsConstructor(onConstructor = @___(@Autowired))
public class ReturnedRestController {
    private final ReturnedService returnedService;

    private final ProductService productService;

    @PostMapping("/getReturnedList")
    public ApiResponse getOrders(@RequestBody SearchVO searchVO) {
        JSONObject jsonObject = new JSONObject();
        List<Returned> list = returnedService.getReturnedList(searchVO);
        jsonObject.put("total", list.size());
        jsonObject.put("returnedList", list);
        return ApiResponse.ofSuccess(jsonObject);
    }

    @GetMapping("/getProductsByPrincipalId")
    public ApiResponse getProductsByPrincipalId(@RequestParam("id")String pcpId){
        JSONObject jsonObject = new JSONObject();
        List<Product> products = returnedService.getProductsByPrincipalId(pcpId);
        jsonObject.put("products",products);
        jsonObject.put("total",products.size());
        return ApiResponse.ofSuccess(jsonObject);
    }
    @GetMapping("/findProductById")
    public ApiResponse findProductById(@RequestParam("id")String id){
        JSONObject jsonObject = new JSONObject();
        Product product = productService.findProductById(id);
        jsonObject.put("product",product);
        return ApiResponse.ofSuccess(jsonObject);
    }
    @PostMapping("/addReturned")
    public ApiResponse addReturned(@RequestBody Returned returned) {

        int i = returnedService.addReturned(returned);
        if (i==1)
            return ApiResponse.success("添加成功！");
        else
            return ApiResponse.fail("添加出现异常！");
    }

    @PostMapping("/deleteReturnedList")
    public ApiResponse deleteReturnedList(@RequestBody List<Returned> returneds) {

        int i = returnedService.deleteReturnedList(returneds);
        if (i==1)
            return ApiResponse.success("删除成功！");
        else
            return ApiResponse.fail("删除出现异常！");
    }

}
