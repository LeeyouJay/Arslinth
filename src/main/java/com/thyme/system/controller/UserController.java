package com.thyme.system.controller;

import com.thyme.common.utils.SecurityUtils;
import com.thyme.system.entity.SysMenu;
import com.thyme.system.entity.SysUser;
import com.thyme.system.service.SysMenuService;
import com.thyme.system.service.SysRoleService;
import com.thyme.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author cuiyating
 * @date 2020/1/2 20:43
 */
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final SysUserService sysUserService;

    private final SysRoleService sysRoleService;

    private final SysMenuService sysMenuService;

    @GetMapping("/list")
    public String index(Model model){
        SysMenu sysMenu = sysMenuService.getByHref("user/list");
        SysMenu parentMenu = sysMenuService.getById(sysMenu.getParentId());
        model.addAttribute("menuName",sysMenu.getMenuName());
        model.addAttribute("parentName",parentMenu.getMenuName());
        return "module/user/user";
    }

    @GetMapping("/update")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    public String update(String id, Model model){
        SysUser sysUser = sysUserService.getById(id);
        String roleName = sysRoleService.getById(sysUser.getId());
        model.addAttribute("sysUser", sysUser);
        model.addAttribute("avatar",sysUser.getAvatar()==null?"/static/css/noAvatar.jpg":"/"+sysUser.getAvatar());
        model.addAttribute("roleName", roleName);
        return "module/user/updateUser";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    public String add(){
        return "module/user/addUser";
    }

    @GetMapping("/changePassword")
    public String changePassword(){
        return "module/user/changePassword";
    }

    @GetMapping("/personal")
    public String personal(Model model){

        AbstractAuthenticationToken token = SecurityUtils.getCurrentUserToken();
        String username =token.getName();
        SysUser sysUser = sysUserService.findByName(username);
        String roleName = sysRoleService.getById(sysUser.getId());
        model.addAttribute("sysUser", JSONObject.fromObject(sysUser));
        model.addAttribute("avatar",sysUser.getAvatar()==null?"/static/css/noAvatar.jpg":"/"+sysUser.getAvatar());
        model.addAttribute("roleName", roleName);
        return "module/user/personal";
    }
}
