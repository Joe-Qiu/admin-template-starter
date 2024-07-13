package com.treeview.controller.basic;

import com.treeview.entity.framework.Rest;
import com.treeview.entity.system.UserInfo;
import com.treeview.enums.system.user.UserStatusEnum;
import com.treeview.enums.system.user.UserTypeEnum;
import com.treeview.service.system.UserInfoService;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class RegisterController extends SuperController{
    @Resource
    private UserInfoService userInfoService;

    @GetMapping(value = "/register")
    public String register() {
        return "system/register";
    }

    @PostMapping("/register")
    @ResponseBody
    public Rest ajaxRegister(String username, String password, String validateCode) {
        if (!CaptchaUtil.ver(validateCode, request)) {
            CaptchaUtil.clear(request);
            return new Rest(201, "验证码不正确");
        }

        final UserInfo userInfo = new UserInfo();
        userInfo.setUserName(username);
        userInfo.setPassWord(password);

        userInfo.setStatus(UserStatusEnum.EFF_USER.getStatus());
        userInfo.setUserType(UserTypeEnum.OUTER_USER.getType());

        userInfoService.saveUser(userInfo);

        return Rest.ok();
    }
}