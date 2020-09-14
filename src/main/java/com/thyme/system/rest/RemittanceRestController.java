package com.thyme.system.rest;

import com.alibaba.fastjson.JSONObject;
import com.thyme.common.base.ApiResponse;
import com.thyme.system.entity.bussiness.PayRecord;
import com.thyme.system.service.PayRecordService;
import com.thyme.system.service.RemittanceService;
import com.thyme.system.vo.RemittanceVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Arslinth
 * @ClassName RemittanceRestController
 * @Description TODO
 * @Date 2020/6/19
 */
@RestController
@RequestMapping("/remittance")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RemittanceRestController {

    private final RemittanceService remittanceService;

    private final PayRecordService payRecordService;

    @GetMapping("/getRemittances")
    public ApiResponse getRemittances(@RequestParam("name")String pcpName){
        JSONObject jsonObject = new JSONObject();
        List<RemittanceVO> remittances = remittanceService.getRemittances(pcpName);
        jsonObject.put("remittances",remittances);
        jsonObject.put("total",remittances.size());
        return ApiResponse.ofSuccess(jsonObject);
    }

    @PostMapping("addPayRecord")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    @ResponseBody
    public ApiResponse addPayRecord(@RequestBody PayRecord payRecord){
        payRecordService.addPayRecord(payRecord);
        return ApiResponse.success("添加成功！");
    }

    @GetMapping("getPayRecords")
    public ApiResponse getPayRecords(@RequestParam("id") String id){
        JSONObject jsonObject = new JSONObject();
        List<PayRecord> payRecords = payRecordService.getByPrincipalId(id);
        jsonObject.put("payRecords",payRecords);
        jsonObject.put("total",payRecords.size());
        return ApiResponse.ofSuccess(jsonObject);
    }
}
