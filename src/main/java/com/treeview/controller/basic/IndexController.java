package com.treeview.controller.basic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping(value = "/")
    public String index() {
        return "index";
    }

    @GetMapping(value = "/main")
    public String main() {
        return "main";
    }
}