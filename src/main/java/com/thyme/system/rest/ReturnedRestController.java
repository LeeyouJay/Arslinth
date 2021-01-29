package com.thyme.system.rest;

import com.alibaba.fastjson.JSONObject;
import com.thyme.common.base.ApiResponse;
import com.thyme.system.entity.bussiness.Returned;
import com.thyme.system.service.impl.ReturnedService;
import com.thyme.system.vo.SearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/returned")
@RestController
@RequiredArgsConstructor(onConstructor = @___(@Autowired))
public class ReturnedRestController {
    private final ReturnedService returnedService;

    @PostMapping("/getReturnedList")
    public ApiResponse getOrders(@RequestBody SearchVO searchVO) {
        JSONObject jsonObject = new JSONObject();
        List<Returned> list = returnedService.getReturnedList();
        jsonObject.put("total", list.size());
        jsonObject.put("orderList", list);
        return ApiResponse.ofSuccess(jsonObject);
    }
}
