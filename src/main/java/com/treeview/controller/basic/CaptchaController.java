package com.treeview.controller.basic;

import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/captcha")
public class CaptchaController {
    @RequestMapping("/image")
    public void image(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final ArithmeticCaptcha captcha = new ArithmeticCaptcha();
        CaptchaUtil.out(captcha, request, response);
    }
}