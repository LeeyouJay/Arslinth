package com.thyme.system.controller;

import com.thyme.system.entity.SysMenu;
import com.thyme.system.entity.SysRole;
import com.thyme.system.service.SysMenuService;
import com.thyme.system.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author cuiyating
 * @date 2020/1/3 15:55
 */
@Controller
@RequestMapping("/role")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleController {

    private final SysMenuService sysMenuService;

    private final SysRoleService sysRoleService;

    @GetMapping("/list")
    public String index(Model model){
        SysMenu sysMenu = sysMenuService.getByHref("user/list");
        SysMenu parentMenu = sysMenuService.getById(sysMenu.getParentId());
        model.addAttribute("menuName",sysMenu.getMenuName());
        model.addAttribute("parentName",parentMenu.getMenuName());
        return "module/role/role";
    }

    @GetMapping("/update")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    public String update(String id,Model model){
        SysRole sysRole = sysRoleService.findByRoleId(id);
        model.addAttribute("sysRole",sysRole);
        return "module/role/updateRole";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    public String add(){
        return "module/role/addRole";
    }
}
