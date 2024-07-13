package com.treeview.controller.basic;

import com.treeview.entity.framework.Rest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/i18n")
public class I18nController {
    @RequestMapping("/setLang")
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