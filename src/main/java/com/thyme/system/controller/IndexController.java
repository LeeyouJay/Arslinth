package com.thyme.system.controller;

import com.thyme.system.entity.SysMenu;
import com.thyme.system.service.OrderService;
import com.thyme.system.service.impl.UpdateLogServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author thyme
 * @ClassName LoginController
 * @Description TODO
 * @Date 2019/12/11 10:50
 */
@Controller
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IndexController {

    private final SessionRegistry sessionRegistry;

    private final UpdateLogServiceImpl updateLogService;

    private final OrderService orderService;

    @RequestMapping("/")
    public String index() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
        return "index";
    }

    @RequestMapping("/console")
    public String home(Model model) {

        Map<String, Double[][]> growth = orderService.showGrowth();
        Map<String, Object> totalMap = orderService.showTotal();
        Map<String, int[]> typeValue = orderService.showTypeValue();
        List<String> keys = new ArrayList<>();
        for (String key : typeValue.keySet()) {
            keys.add(key);
        }

        model.addAttribute("pre", growth.get("pre"));
        model.addAttribute("cur", growth.get("cur"));
        model.addAttribute("totalStock", totalMap.get("totalStock"));
        model.addAttribute("totalPrice", totalMap.get("totalPrice"));
        model.addAttribute("totalValue", totalMap.get("totalValue"));
        model.addAttribute("totalEarns", totalMap.get("totalEarns"));
        model.addAttribute("typeName", keys);
        model.addAttribute("typeValue", typeValue);
        return "analysis";
    }

    @RequestMapping("/admin")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String admin() {
        return "是管理员";
    }

    @RequestMapping("/user")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_USER')")
    public String user(){
        return "是普通用户";
    }
}
