package com.thyme.system.controller;

import com.thyme.system.entity.SysMenu;
import com.thyme.system.entity.bussiness.Principal;
import com.thyme.system.entity.bussiness.Stock;
import com.thyme.system.service.StockService;
import com.thyme.system.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Arslinth
 * @ClassName ProductListController
 * @Description 进货记录
 * @Date 2020/6/9
 */
@Controller
@RequestMapping("/Import")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImportController {

    private final SysMenuService sysMenuService;

    private final StockService stockService;

    @GetMapping("/list")
    public String productList(Model model){
        SysMenu sysMenu = sysMenuService.getByHref("Import/list");
        SysMenu parentMenu = sysMenuService.getById(sysMenu.getParentId());
        model.addAttribute("menuName",sysMenu.getMenuName());
        model.addAttribute("parentName",parentMenu.getMenuName());
        return "module/Import/ImportList";
    }

    @GetMapping("/remittance")
    public String tradingRecord(Model model){
        SysMenu sysMenu = sysMenuService.getByHref("Import/remittance");
        SysMenu parentMenu = sysMenuService.getById(sysMenu.getParentId());
        model.addAttribute("menuName",sysMenu.getMenuName());
        model.addAttribute("parentName",parentMenu.getMenuName());
        return "module/Import/remittance";
    }


    @GetMapping("/principal")
    public String dealers(Model model){
        SysMenu sysMenu = sysMenuService.getByHref("Import/principal");
        SysMenu parentMenu = sysMenuService.getById(sysMenu.getParentId());
        model.addAttribute("menuName",sysMenu.getMenuName());
        model.addAttribute("parentName",parentMenu.getMenuName());
        return "module/Import/principal";
    }

    @GetMapping("/updatePrincipal")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    public String updatePrincipal(Model model, @RequestParam("id") String id){
        Principal principal = stockService.getPrincipalById(id);
        model.addAttribute("principal",principal);
        return "module/Import/updatePrincipal";
    }

    @GetMapping("/updateStock")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    public String updateStock(Model model, @RequestParam("id") String id){
        Stock stock = stockService.getStockById(id);
        model.addAttribute("stock",stock);
        return "module/Import/updateImport";
    }

    @GetMapping("/payDetails")
    public String payDetails(@RequestParam("principalId") String principalId,Model model){
        model.addAttribute("principalId",principalId);
        return "module/Import/payDetails";
    }
}
