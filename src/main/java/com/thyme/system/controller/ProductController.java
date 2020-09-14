package com.thyme.system.controller;

import com.thyme.system.entity.SysMenu;
import com.thyme.system.entity.bussiness.Product;
import com.thyme.system.entity.bussiness.ProductImg;
import com.thyme.system.service.ImageService;
import com.thyme.system.service.ProductService;
import com.thyme.system.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Arslinth
 * @ClassName ProductController
 * @Description 产品信息
 * @Date 2020/6/17
 */
@Controller
@RequestMapping("/product")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductController {

    private final SysMenuService sysMenuService;

    private final ProductService productService;

    private final ImageService imageService;

    @GetMapping("/list")
    public String productList(Model model){
        SysMenu sysMenu = sysMenuService.getByHref("product/list");
        SysMenu parentMenu = sysMenuService.getById(sysMenu.getParentId());
        model.addAttribute("menuName",sysMenu.getMenuName());
        model.addAttribute("parentName",parentMenu.getMenuName());
        return "module/product/productList";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    public String add(){
        return "module/product/addProduct";
    }

    @GetMapping("/update")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    public String update(@RequestParam("id")String id,Model model){
        Product product = productService.findProductById(id);
        List<ProductImg> imgByName = imageService.findImgByName(product.getPdName());
        model.addAttribute("product",product);
        if(imgByName.size()==0 || imgByName.get(0).getUrl()==null || "".equals(imgByName.get(0).getUrl()))
            model.addAttribute("ImgUrl","/static/css/noPicture.jpg");
        else
            model.addAttribute("ImgUrl",imgByName.get(0).getUrl());
        return "module/product/updateProduct";
    }
    @GetMapping("/addType")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    public String addType(){
        return "module/product/addType";
    }

}
