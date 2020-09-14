package com.thyme.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/stuData")
public class StuScoController {


    @GetMapping("/list")
    public String list(){
        return "module/stuData/stuScore";
    }
}
