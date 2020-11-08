package com.thyme.system.rest;

import com.thyme.common.base.ApiResponse;
import com.thyme.system.entity.bussiness.UpdateLog;
import com.thyme.system.service.impl.UpdateLogServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Arslinth
 * @ClassName ConsloRestController
 * @Description TODO
 * @Date 2020/11/10
 */
@RestController
@RequestMapping("/console")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConsloRestController {
    private final UpdateLogServiceImpl updateLogService;

    @PostMapping("/addLog")
    public ApiResponse addLog(@RequestBody UpdateLog updateLog) {
        updateLogService.addLog(updateLog);
        return ApiResponse.success("添加成功！");
    }

    @PostMapping("/updateLog")
    public ApiResponse updateLog(@RequestBody UpdateLog updateLog) {
        int i = updateLogService.updateLog(updateLog);
        if (i == 1) {
            return ApiResponse.success("修改成功！");
        } else
            return ApiResponse.fail("修改出现异常！");

    }
}
