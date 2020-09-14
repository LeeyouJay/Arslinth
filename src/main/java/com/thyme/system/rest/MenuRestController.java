package com.thyme.system.rest;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thyme.common.base.ApiResponse;
import com.thyme.common.utils.SecurityUtils;
import com.thyme.common.utils.UUIDUtils;
import com.thyme.system.entity.SysMenu;
import com.thyme.system.entity.SysUser;
import com.thyme.system.service.SysMenuService;
import com.thyme.system.service.SysUserService;
import com.thyme.system.vo.*;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author thyme
 * @ClassName MenuRestController
 * @Description 菜单RestController
 * @Date 2019/12/24 14:50
 */
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MenuRestController {

    private final SysMenuService sysMenuService;

    private final SysUserService sysUserService;
    @GetMapping("/getMenuList")
    public ApiResponse getMenuList() {
        //获取当前用户登录用户
        //Authentication userAuthentication = SecurityUtils.getCurrentUserAuthentication();
        //String name = userAuthentication.getName();
        String name = SecurityUtils.getCurrentUserToken().getName();
        SysUser sysUser = sysUserService.findByName(name);

        List<MenuVo> menuVoList = sysMenuService.getMenuByUser(name);
        JSONObject data = new JSONObject(16);
        data.put("name", sysUser.getNickName());
        data.put("avatar",sysUser.getAvatar()==null?"static/css/noAvatar.jpg":sysUser.getAvatar());
        data.put("menuList", menuVoList);
        return ApiResponse.ofSuccess(data);
    }

    @GetMapping("/getMenuInfo")
    public ApiResponse getMenuInfo(@RequestParam("page") int page,
                                   @RequestParam("page_size") int pageSize) {
        JSONObject jsonObject = new JSONObject();

        /*page = (page -1) * pageSize;*/
        List<MenuListVo> listVoList = new LinkedList<>();
        IPage<SysMenu> firstMenu = sysMenuService.findFirstMenu(new Page(page, pageSize));
        //组装数据
        List<SysMenu> firstMenuList = firstMenu.getRecords();
        for (SysMenu sysMenu : firstMenuList) {
            List<SysMenu> secondMenu = sysMenuService.findByParentId(sysMenu.getId());
            listVoList.add(MenuListVo.builder().id(sysMenu.getId())
                    .children(secondMenu)
                    .isShow(sysMenu.getIsShow())
                    .menuCode(sysMenu.getMenuCode())
                    .menuHref(sysMenu.getMenuHref())
                    .menuIcon(sysMenu.getMenuIcon())
                    .menuLevel(sysMenu.getMenuLevel())
                    .menuName(sysMenu.getMenuName())
                    .menuWeight(sysMenu.getMenuWeight()).build());
        }
        jsonObject.put("total", firstMenu.getTotal());
        jsonObject.put("page", firstMenu.getCurrent());
        jsonObject.put("page_size", firstMenu.getSize());
        jsonObject.put("menuList", listVoList);
        return ApiResponse.ofSuccess(jsonObject);
    }

    @GetMapping("/deleteMenu")
    public ApiResponse deleteMenu(@RequestParam("id") String id) {
        int i = sysMenuService.deleteMenuById(id);
        if ( i> 0) {
            return ApiResponse.success("删除成功！");
        } else if (i==0)
            return ApiResponse.ofMessage(403, "操作拒绝:存在子菜单！");
        else
            return ApiResponse.ofMessage(403, "权限不足！");
    }

    @GetMapping("/updateSwitch")
    public ApiResponse updateSwitch(@RequestParam("id") String id, @RequestParam("isShow") String isShow) {

        if (sysMenuService.updateShow(id, isShow) > 0) {
            return ApiResponse.success("成功！");
        } else
            return ApiResponse.fail("操作拒绝！");
    }

    @PostMapping("/updateMenu")
    @ResponseBody
    public ApiResponse updateMenu(@RequestBody SysMenuNameVO sysMenuNameVO) {
        JSONObject jsonObject = new JSONObject();
        SysMenuVO sysMenu = new SysMenuVO(sysMenuNameVO.getId(),
                sysMenuNameVO.getParentId(),
                sysMenuNameVO.getMenuName(), sysMenuNameVO.getMenuCode(),
                sysMenuNameVO.getMenuHref(), sysMenuNameVO.getMenuIcon(),
                sysMenuNameVO.getMenuLevel(), sysMenuNameVO.getMenuWeight(),
                sysMenuNameVO.getIsShow(), null, null);
        try {
            if (sysMenuService.updateMenu(sysMenu) > 0) {
                jsonObject.put("code", 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", 500);
        }
        return ApiResponse.ofSuccess(jsonObject);
    }

    @PostMapping("/addMenu")
    @ResponseBody
    public ApiResponse addMenu(@RequestBody SysMenuNameVO sysMenuNameVO) {
        SysMenu menu = sysMenuService.getByName(sysMenuNameVO.getMenuName(), sysMenuNameVO.getMenuCode(), sysMenuNameVO.getMenuHref());
        if (menu == null) {
            String authName = SecurityUtils.getCurrentUserToken().getName();
            SysMenuVO sysMenuVO = new SysMenuVO(UUIDUtils.getUUID(),
                    sysMenuNameVO.getParentId(), sysMenuNameVO.getMenuName(),
                    sysMenuNameVO.getMenuCode(), sysMenuNameVO.getMenuHref(),
                    sysMenuNameVO.getMenuIcon(), sysMenuNameVO.getMenuLevel(),
                    sysMenuNameVO.getMenuWeight(), sysMenuNameVO.getIsShow(), new Date(), authName);
            try {
                if (sysMenuService.addMenu(sysMenuVO) > 0) {
                    return ApiResponse.success("添加成功！");
                } else
                    return ApiResponse.fail("添加失败！");
            } catch (Exception e) {
                e.printStackTrace();
                return ApiResponse.fail("添加出现异常！");
            }
        } else {
            return ApiResponse.ofMessage(403, "菜单名称或别名重复！");
        }

    }

    @GetMapping("/getMenuLevel")
    public ApiResponse getMenuLevel() {
        JSONObject jsonObject = new JSONObject();
        List<String> menuLevel = sysMenuService.getMenuLevel();
        jsonObject.put("menuLevel", menuLevel);
        return ApiResponse.ofSuccess(jsonObject);
    }

    @GetMapping("/getPreviousMenu")
    public ApiResponse getPreviousMenu(@RequestParam("menuLevel") String menuLevel) {
        JSONObject jsonObject = new JSONObject();
        List<MenuNameVO> menuNames = sysMenuService.getPreviousMenu(String.valueOf((Integer.parseInt(menuLevel) - 1)));
        jsonObject.put("menuNames", menuNames);
        return ApiResponse.ofSuccess(jsonObject);
    }

    @GetMapping("/getSearchMenu")
    public ApiResponse searcheMenu(@RequestParam("page") int page,
                                   @RequestParam("page_size") int pageSize,
                                   @RequestParam("menu_name") String menuName) {
        JSONObject jsonObject = new JSONObject();
        IPage<SysMenu> menuIPage = sysMenuService.searchMenu(new Page(page, pageSize), menuName);
        List<SysMenu> menuList = menuIPage.getRecords();

        jsonObject.put("total", menuIPage.getTotal());
        jsonObject.put("page", menuIPage.getCurrent());
        jsonObject.put("page_size", menuIPage.getSize());
        jsonObject.put("menuList", menuList);
        return ApiResponse.ofSuccess(jsonObject);
    }
}
