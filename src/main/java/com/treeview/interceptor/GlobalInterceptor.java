package com.treeview.interceptor;

import com.treeview.entity.system.CurrUser;
import com.treeview.entity.system.TreeMenu;
import com.treeview.entity.system.UserInfo;
import com.treeview.service.monitor.OnlineInfoService;
import com.treeview.service.system.MenuConfigService;
import com.treeview.utils.ShiroUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @author joe.qiu on 2020/03/06
 * @version V1.0.0
 *
 * 全局拦截器
 */
@Slf4j
@Component
public class GlobalInterceptor implements HandlerInterceptor {
    @Lazy
    @Resource
    private MenuConfigService menuConfigService;

    @Value("${spring.root.context}")
    private String rootContext;

    @Lazy
    @Resource
    private OnlineInfoService onlineInfoService;

    @Resource
    private ThreadPoolTaskExecutor serviceTaskExecutor;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            request.setAttribute("rootContext", rootContext);

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

            serviceTaskExecutor.submit(() ->{
                String sessionId = request.getSession().getId();
                onlineInfoService.updateUserLatestVisitTime(sessionId, new Date());
            });
        }

        /**
         * 通过拦截
         */
        return true;
    }
}