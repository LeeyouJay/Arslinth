package com.thyme.system.rest;
import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thyme.common.base.ApiResponse;
import com.thyme.common.utils.SecurityUtils;
import com.thyme.common.utils.UUIDUtils;
import com.thyme.system.entity.SysMenu;
import com.thyme.system.entity.SysMenuRole;
import com.thyme.system.entity.SysRole;
import com.thyme.system.service.SysMenuRoleService;
import com.thyme.system.service.SysMenuService;
import com.thyme.system.service.SysRoleService;
import com.thyme.system.vo.MenuListVo;
import com.thyme.system.vo.RoleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author cuiyating
 * @date 2020/1/3 15:45
 */
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleRestController {

    private final SysRoleService sysRoleService;

    private final SysMenuService sysMenuService;

    private final SysMenuRoleService sysMenuRoleService;

    @GetMapping("/getRoleInfo")
    public ApiResponse getRoleInfo(@RequestParam("page") int page,
                                   @RequestParam("page_size") int pageSize) {
        JSONObject jsonObject = new JSONObject();
        IPage<SysRole> sysRoleList = sysRoleService.getAll(new Page(page, pageSize));
        jsonObject.put("total",sysRoleList.getTotal());
        jsonObject.put("page",sysRoleList.getCurrent());
        jsonObject.put("page_size",sysRoleList.getSize());
        jsonObject.put("sysRoleList",sysRoleList.getRecords());
        return ApiResponse.ofSuccess(jsonObject);
    }

    @GetMapping("/deleteRole")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    @Transactional(rollbackFor={RuntimeException.class, Exception.class})
    public ApiResponse deleteRole(@RequestParam("id")String id){
        JSONObject jsonObject = new JSONObject();
        try{
            sysMenuRoleService.deleteByRoleId(id);
            sysRoleService.deleteById(id);
            jsonObject.put("code",200);
        }catch (Exception e) {
            jsonObject.put("code", 500);
            e.printStackTrace();
        }
        return ApiResponse.ofSuccess(jsonObject);
    }

    @PostMapping("/updateRole")
    @ResponseBody
    public ApiResponse updateRole(@RequestBody RoleVO roleVO){
        int i = sysRoleService.updateById(roleVO);
        if (i==1)
            return ApiResponse.success("修改成功！");
        else if (i==0)
            return ApiResponse.fail("操作拒绝！");
        else
            return ApiResponse.fail("出现异常！");
    }

    @PostMapping("/addRole")
    @ResponseBody
    @Transactional(rollbackFor={RuntimeException.class, Exception.class})
    public ApiResponse addRole(@RequestBody RoleVO roleVO){
        JSONObject jsonObject = new JSONObject();
        try{
            if("ROLE_DEVELOPER".equals(roleVO.getAuthority())){
                return ApiResponse.fail("操作拒绝！");
            }
            SysRole role = sysRoleService.getByName(roleVO.getName());
            if (role == null){
                String id = UUIDUtils.getUUID();
                for (String menuId : roleVO.getIds()){
                    SysMenuRole sysMenuRole = new SysMenuRole(menuId, id);
                    sysMenuRoleService.addMenuRole(sysMenuRole);
                }
                SysRole sysRole = new SysRole(id, roleVO.getName(), roleVO.getAuthority(), new Date());
                sysRoleService.insert(sysRole);
                jsonObject.put("code",200);
            } else {
                // 501 角色已存在
                jsonObject.put("code",501);
            }
        }catch (Exception e){
            jsonObject.put("code",500);
            e.printStackTrace();
        }
        return ApiResponse.ofSuccess(jsonObject);
    }

    @GetMapping("/getRoleMenu")
    public ApiResponse getRoleMenu(@RequestParam("roleId")String roleId){
        JSONObject jsonObject = new JSONObject();
        List<String> parentIds = sysMenuService.getRoleMenu(roleId);
        List<String> roleMenuIds = sysMenuRoleService.getAllMenuId(roleId);
        AbstractAuthenticationToken token = SecurityUtils.getCurrentUserToken();
        Collection<GrantedAuthority> authorities = token.getAuthorities();
        String authority="";
        for (GrantedAuthority g:authorities){
            authority=g.getAuthority().toString();
        }
        jsonObject.put("ids", roleMenuIds);
        jsonObject.put("parentIds", parentIds);
        jsonObject.put("menuList",sysMenuService.getAllMenus(authority));
        return ApiResponse.ofSuccess(jsonObject);
    }

}
