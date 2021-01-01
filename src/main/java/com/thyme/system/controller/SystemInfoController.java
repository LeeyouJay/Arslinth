package com.thyme.system.controller;

import com.thyme.system.entity.SysMenu;
import com.thyme.system.service.SysMenuService;
import com.thyme.system.service.impl.UpdateLogServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author thyme
 * @ClassName SystemInfoController
 * @Description TODO
 * @Date 2019/12/27 16:57
 */
@Controller
@RequestMapping("/system")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SystemInfoController {

    private final UpdateLogServiceImpl updateLogService;

    @GetMapping("/serverInfo")
    public String serverInfo() {
        return "module/system/server";
    }

    @GetMapping("/dateLog")
    public String updateLog(Model model) {
        model.addAttribute("list", updateLogService.getLogList());
        return "module/system/dateLog";
    }

    @RequestMapping("/updateLog")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER')")
    public String updateLog(@RequestParam("id") String id, Model model) {
        model.addAttribute("updateLog", updateLogService.findById(id));
        return "updateLog";
    }
}
