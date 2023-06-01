package com.qiaoqing.springboot.demosecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DemoController {

    @GetMapping("/")
    public String showHome() {
        return "home";// home.html
    }

    // add a request mapping for /leaders
    @GetMapping("/leaders")
    public String showLeaders() {
        return "leaders";// leaders.html
    }

    // add a request mapping for /systems
    @GetMapping("/systems")
    public String showSystems() {
        return "systems"; // systems.html
    }
}
