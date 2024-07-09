package com.treeview.controller.base;

import com.treeview.entity.framework.Rest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/i18n")
public class I18nController {
    @RequestMapping("/setLang")
    @ResponseBody
    public Rest setLang(HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isNotEmpty(request.getParameter("lang"))) {
            String lang = request.getParameter("lang");

            final Cookie cookie = new Cookie("lang", lang);
            cookie.setMaxAge(24 * 60 * 60);
            cookie.setHttpOnly(true);
            cookie.setPath("/");

            response.addCookie(cookie);
        }

        return Rest.ok();
    }
}