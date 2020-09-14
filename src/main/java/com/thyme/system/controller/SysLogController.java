package com.thyme.system.controller;

import com.thyme.system.entity.SysMenu;
import com.thyme.system.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author thyme
 * @ClassName SysLogController
 * @Description TODO
 * @Date 2020/1/9 16:24
 */
@Controller
@RequestMapping("/sys_log")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysLogController {

    private final SysMenuService sysMenuService;


    @GetMapping("/list")
    public String index(Model model){
        SysMenu sysMenu = sysMenuService.getByHref("sys_log/list");
        SysMenu parentMenu = sysMenuService.getById(sysMenu.getParentId());
        model.addAttribute("menuName",sysMenu.getMenuName());
        model.addAttribute("parentName",parentMenu.getMenuName());
        return "module/syslog/syslog";
    }
}
