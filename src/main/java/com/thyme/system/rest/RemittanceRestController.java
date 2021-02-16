package com.thyme.system.rest;

import com.alibaba.fastjson.JSONObject;
import com.thyme.common.base.ApiResponse;
import com.thyme.common.utils.UUIDUtils;
import com.thyme.system.entity.bussiness.PayRecord;
import com.thyme.system.entity.bussiness.Principal;
import com.thyme.system.entity.bussiness.Ticket;
import com.thyme.system.service.PayRecordService;
import com.thyme.system.service.RemittanceService;
import com.thyme.system.service.StockService;
import com.thyme.system.service.UploadService;
import com.thyme.system.vo.RemittanceVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    private final StockService stockService;

    private final RemittanceService remittanceService;

    private final PayRecordService payRecordService;

    private final UploadService uploadService;

    @GetMapping("/getRemittances")
    public ApiResponse getRemittances(@RequestParam("name")String pcpName){
        JSONObject jsonObject = new JSONObject();
        List<RemittanceVO> remittances = remittanceService.getRemittances(pcpName);


        jsonObject.put("remittances",remittances);
        jsonObject.put("total",remittances.size());
        return ApiResponse.ofSuccess(jsonObject);
    }

    @PostMapping("/addPayRecord")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    @ResponseBody
    public ApiResponse addPayRecord(@RequestBody PayRecord payRecord){
        payRecordService.addPayRecord(payRecord);
        return ApiResponse.success("添加成功！");
    }

    @GetMapping("/getPayRecords")
    public ApiResponse getPayRecords(@RequestParam("id") String id){
        JSONObject jsonObject = new JSONObject();
        List<PayRecord> payRecords = payRecordService.getByPrincipalId(id);
        jsonObject.put("payRecords",payRecords);
        jsonObject.put("total",payRecords.size());
        return ApiResponse.ofSuccess(jsonObject);
    }

    @PostMapping("/updateRecords")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    @ResponseBody
    public ApiResponse updateRecords(@RequestBody PayRecord payRecord){

        int i = payRecordService.updateRecords(payRecord);
        if (i>0)
            return ApiResponse.success("修改成功！");
        else
            return ApiResponse.fail("修改出现异常！");
    }

    @GetMapping("/deleteRecord")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    public ApiResponse deleteRecord(@RequestParam("id") String id){
        int i = payRecordService.deleteRecord(id);
        if (i>0)
            return ApiResponse.success("删除成功！");
        else
            return ApiResponse.fail("删除出现异常！");
    }


    @PostMapping("/addTicket")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    @ResponseBody
    public ApiResponse addTicket(MultipartFile file, String principalId){

        try {
            Principal principal = stockService.getPrincipalById(principalId);

            String ticketId = UUIDUtils.getSixteenUUID();

            String ticketImgUrl = uploadService.uploadImg(file, principal.getPcpName()+ticketId);

            Ticket ticket = Ticket.builder()
                    .id(ticketId)
                    .principalId(principalId)
                    .url(ticketImgUrl).build();
            payRecordService.addTicket(ticket);
            return ApiResponse.success("添加成功！");

        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.fail("上传出现异常！");
        }
    }

    @GetMapping("/getTickets")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    public ApiResponse getTickets(@RequestParam("principalId") String principalId){
        JSONObject jsonObject = new JSONObject();
        List<Ticket> tickets = payRecordService.getTickets(principalId);
        jsonObject.put("tickets",tickets);
        return ApiResponse.ofSuccess(jsonObject);
    }
    @GetMapping("/delTicket")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    public ApiResponse delTicket(@RequestParam("id") String id){

        int i = payRecordService.deleteTicket(id);
        if (i>0)
            return ApiResponse.success("删除成功！");
        else
            return ApiResponse.fail("删除出现异常！");
    }

}
