package com.treeview.interceptor;

import com.treeview.configurations.TemplateProperties;
import com.treeview.entity.system.CurrUser;
import com.treeview.entity.system.TreeMenu;
import com.treeview.entity.system.UserInfo;
import com.treeview.service.system.MenuConfigService;
import com.treeview.utils.ShiroUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@Component
public class GlobalInterceptor implements HandlerInterceptor {
    @Lazy
    @Resource
    private MenuConfigService menuConfigService;

    @Resource
    private TemplateProperties templateProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            request.setAttribute("rootContext", templateProperties.getRootContext());
            request.setAttribute("company", templateProperties.getCompany());
            request.setAttribute("multiLanguage", templateProperties.getMultiLanguage());

            Cookie[] cookies = request.getCookies();

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("lang")) {
                        request.setAttribute("lang", cookie.getValue());
                    }
                }
            }

            String res = request.getParameter("p");
            if (StringUtils.isNotBlank(res)) {
                request.getSession().setAttribute("res", res);
            }

            String cur = request.getParameter("t");
            if (StringUtils.isNotBlank(cur)) {
                request.getSession().setAttribute("cur", cur);
            }

            final UserInfo userInfo = ShiroUtil.getSessionUser();

            if(userInfo != null && userInfo.getId() != null){
                List<TreeMenu> treeMenus = this.menuConfigService.selectTreeMenuByUserId(userInfo.getId());
                request.setAttribute("treeMenus", treeMenus);

                final CurrUser currUser = CurrUser.builder()
                        .id(userInfo.getId())
                        .userName(userInfo.getUserName())
                        .nickName(userInfo.getNickName()).build();

                request.setAttribute("currUser", currUser);
            }
        }

        /**
         * 通过拦截
         */
        return true;
    }
}