package com.treeview.controller.base;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author joe.qiu on 2020/03/06
 * @version V1.0.0
 *
 * 错误控制器
 */
@Controller
@RequestMapping("/error")
public class ErrorController {
    @RequestMapping(value = "/{code}")
    public String index(@PathVariable String code, Model model) {
        return "error/" + code;
    }
}