package com.thyme.system.rest;

import com.alibaba.fastjson.JSONObject;
import com.thyme.common.base.ApiResponse;
import com.thyme.system.entity.bussiness.Principal;
import com.thyme.system.entity.bussiness.Stock;
import com.thyme.system.service.StockService;
import com.thyme.system.service.UploadService;
import com.thyme.system.vo.SearchVO;
import com.thyme.system.vo.StockVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author Arslinth
 * @ClassName StockRestController
 * @Description TODO
 * @Date 2020/6/12
 */
@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StockRestController {

    private final StockService stockService;

    private final UploadService uploadService;

    @PostMapping("/getStocks")
    public ApiResponse getStock(@RequestBody SearchVO searchVO) {
        JSONObject jsonObject = new JSONObject();
        List<StockVO> stocks = stockService.getStocks(searchVO);
        jsonObject.put("total", stocks.size());
        jsonObject.put("stockList", stocks);
        return ApiResponse.ofSuccess(jsonObject);
    }

    @PostMapping("/addStock")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    @ResponseBody
    public ApiResponse addStock(@RequestBody Stock stock) {

        int i = stockService.addStock(stock);

        if (i == 1)
            return ApiResponse.success("添加成功！");
        else
            return ApiResponse.fail("添加异常！");
    }

    @PostMapping("/updateStock")
    @ResponseBody
    public ApiResponse updateStock(@RequestBody Stock stock) {
        int i = stockService.updateStock(stock);
        if (i == 1)
            return ApiResponse.success("更新成功！");
        else
            return ApiResponse.success("更新出现异常！");
    }

    @PostMapping("/deleteStocks")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    @ResponseBody
    public ApiResponse deleteStocks(@RequestBody List<Map<String,Object>> checkList) {
        int i = stockService.deleteStocksByIds(checkList);
        if (i == 1) {
            return ApiResponse.success("删除成功！");
        } else
            return ApiResponse.fail("删除出现异常！");

    }


    @GetMapping("/getPrincipals")
    public ApiResponse getPrincipal(@RequestParam("name") String pcpName) {
        JSONObject jsonObject = new JSONObject();
        List<Principal> principals = stockService.getPrincipals(pcpName);
        jsonObject.put("total", principals.size());
        jsonObject.put("principals", principals);
        return ApiResponse.ofSuccess(jsonObject);
    }

    @PostMapping("/addPrincipal")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    @ResponseBody
    public ApiResponse addPrincipal(@RequestBody Principal principal) {

        int i = stockService.addPrincipal(principal);

        if (i == 0)
            return ApiResponse.ofMessage(403, "存在相同负责人名称！");
        else if (i == 1)
            return ApiResponse.success("添加成功！");
        else
            return ApiResponse.fail("添加异常！");


    }

    @PostMapping("/updatePrincipal")
    @ResponseBody
    public ApiResponse updatePrincipal(@RequestBody Principal principal) {

        int i = stockService.updatePrincipal(principal);

        if (i == 0)
            return ApiResponse.ofMessage(403, "存在相同负责人名称！");
        else if (i == 1)
            return ApiResponse.success("修改成功！");
        else
            return ApiResponse.fail("修改出现异常！");
    }

    @GetMapping("/delPrincipal")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    public ApiResponse deletePrincipal(@RequestParam("id") String id) {

        int i = stockService.deletePrincipal(id);

        if (i == 0)
            return ApiResponse.ofMessage(403, "此负责人仍有种子入库数据，操作拒绝！");
        else if (i == 1)
            return ApiResponse.success("删除成功！");
        else
            return ApiResponse.fail("修改出现异常！");
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    @ResponseBody
    public ApiResponse upload(MultipartFile file, String id) {
        JSONObject jsonObject = new JSONObject();
        List<String> messages = uploadService.uploadExcel(file, id);
        if (messages.size() > 0) {
            jsonObject.put("inCode", 300);
            jsonObject.put("inMessage", messages);
            return ApiResponse.ofSuccess(jsonObject);
        } else {
            jsonObject.put("inCode", 200);
            jsonObject.put("inMessage", "导入数据成功！");
            return ApiResponse.ofSuccess(jsonObject);
        }


    }

}
