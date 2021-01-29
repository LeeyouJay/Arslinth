package com.thyme.system.controller;

import com.thyme.system.entity.SysMenu;
import com.thyme.system.service.OrderService;
import com.thyme.system.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Arslinth
 * @ClassName SellController
 * @Description TODO
 * @Date 2020/7/11
 */
@Controller
@RequestMapping("/sales")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SalesController {

    private final SysMenuService sysMenuService;

    private final OrderService orderService;

    @GetMapping("/management")
    public String dealers(Model model){
        SysMenu sysMenu = sysMenuService.getByHref("sales/management");
        SysMenu parentMenu = sysMenuService.getById(sysMenu.getParentId());
        model.addAttribute("menuName",sysMenu.getMenuName());
        model.addAttribute("parentName",parentMenu.getMenuName());
        return "module/sales/management";
    }

    @GetMapping("/orderDetails")
    public String orderDetails(@RequestParam("OrderId") String OrderId, Model model){
        model.addAttribute("OrderId",OrderId);
        return "module/sales/orderDetails";
    }
    @GetMapping("/salesDetails")
    public String salesDetails(@RequestParam("OrderId") String OrderId, Model model){
        model.addAttribute("OrderId",OrderId);
        return "module/sales/salesDetails";
    }

    @GetMapping("/addDistrict")
    public String addDistrict(){
        return "module/sales/addDistrict";
    }

    @GetMapping("/returnList")
    public String returnList(Model model){
        SysMenu sysMenu = sysMenuService.getByHref("sales/returnList");
        SysMenu parentMenu = sysMenuService.getById(sysMenu.getParentId());
        model.addAttribute("menuName",sysMenu.getMenuName());
        model.addAttribute("parentName",parentMenu.getMenuName());
        return "module/sales/returnList";
    }
}
