package com.thyme.system.rest;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thyme.common.base.ApiResponse;
import com.thyme.common.utils.SecurityUtils;
import com.thyme.system.entity.SysLog;
import com.thyme.system.service.SysLogService;
import com.thyme.system.vo.MenuVo;
import com.thyme.system.vo.SysLogVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author thyme
 * @ClassName SysLogRestController
 * @Description TODO
 * @Date 2020/1/13 10:46
 */
@RestController
@RequestMapping("/sysLog")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysLogRestController {

    private final SysLogService sysLogService;

    @PostMapping("/getSysLogList")
    public ApiResponse getSysLogList(@RequestBody SysLogVO sysLogVO) {
        List<SysLog> sysLogList = sysLogService.findSysLogPage(sysLogVO);
        JSONObject data = new JSONObject(16);
        data.put("total", sysLogList.size());
        data.put("sysLogList", sysLogList);
        return ApiResponse.ofSuccess(data);
    }

    @PostMapping("/deleteLogs")
    @PreAuthorize("hasRole('ROLE_DEVELOPER')")
    public ApiResponse deleteLogs(@RequestBody List<String> ids){
        int i = sysLogService.deleteSysLogByIds(ids);
        if (i > 0) {
            return ApiResponse.success("删除成功！");
        } else
            return ApiResponse.fail("删除出现异常！");
    }

}
