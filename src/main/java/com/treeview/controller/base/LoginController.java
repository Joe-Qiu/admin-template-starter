package com.treeview.controller.base;

import com.treeview.entity.framework.Rest;
import com.wf.captcha.utils.CaptchaUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author joe.qiu on 2022/5/1
 * @version V1.0.0
 */
@Controller
public class LoginController extends SuperController {
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String index() {
        return "system/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public Rest ajaxLogin(String username, String password, String validateCode, Boolean rememberMe) {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        Subject subject = SecurityUtils.getSubject();

        if (!CaptchaUtil.ver(validateCode, request)) {
            CaptchaUtil.clear(request);
            return new Rest(201, "验证码不正确");
        }

        try {
            subject.login(token);
            return Rest.ok();
        } catch (AuthenticationException e) {
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage())) {
                msg = e.getMessage();
            }
            return Rest.failure(msg);
        }
    }
}