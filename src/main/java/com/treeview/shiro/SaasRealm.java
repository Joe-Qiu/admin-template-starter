package com.treeview.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.treeview.entity.monitor.OnlineInfo;
import com.treeview.entity.system.UserInfo;
import com.treeview.enums.system.user.UserStatusEnum;
import com.treeview.service.monitor.OnlineInfoService;
import com.treeview.service.system.RoleMenuService;
import com.treeview.service.system.UserInfoService;
import com.treeview.service.system.UserRoleService;
import com.treeview.utils.ShiroUtil;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.WebSubject;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class SaasRealm extends AuthorizingRealm {
    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private RoleMenuService roleMenuService;

    @Resource
    private OnlineInfoService onlineInfoService;

    @Resource
    private ThreadPoolTaskExecutor serviceTaskExecutor;

    public SaasRealm() {
        this.setName("ArcherRealm");
        CredentialsMatcherSso hcm = new CredentialsMatcherSso();
        this.setCredentialsMatcher(hcm);
    }

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();
        String password = "";

        if (upToken.getPassword() != null) {
            password = new String(upToken.getPassword());
        }

        final QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", username);

        final UserInfo userInfo = this.userInfoService.getOne(queryWrapper);

        if (userInfo == null) {
            throw new UnknownAccountException();
        } else if (UserStatusEnum.EXP_USER.getStatus().equals(userInfo.getStatus())) {
            throw new LockedAccountException();
        } else if (!StringUtils.equals(userInfo.getPassWord(), userInfoService.encryptPassWord(username, password, userInfo.getSalt()))) {
            throw new AuthenticationException();
        } else {
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userInfo, "", userInfo.getUserName());
            // 记录用户登录信息
            recordUserOnlineInfo(userInfo);
            return info;
        }
    }

    private void recordUserOnlineInfo(UserInfo userInfo) {
        try {
            final Subject subject = SecurityUtils.getSubject();

            if (subject instanceof WebSubject) {
                final HttpServletRequest request = (HttpServletRequest) ((WebSubject) subject).getServletRequest();
                final UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));

                final OnlineInfo onlineInfo = new OnlineInfo();
                onlineInfo.setUserName(userInfo.getUserName());
                onlineInfo.setSession(String.valueOf(subject.getSession().getId()));
                onlineInfo.setIpAddr(ShiroUtil.getRemoteAddress());
                // 获取客户端浏览器
                onlineInfo.setBrowser(userAgent.getBrowser().getName());
                // 获取客户端操作系统
                onlineInfo.setOsName(userAgent.getOperatingSystem().getName());

                onlineInfo.setLastLoginTime(new Date());
                onlineInfo.setLastVisitTime(new Date());

                if (subject.getSession() != null) {
                    subject.getSession().setAttribute("onlineInfo", onlineInfo);
                }

                serviceTaskExecutor.submit(() -> onlineInfoService.save(onlineInfo));
            }
        } catch (Exception e) {
            log.error("Exception", e);
        }
    }

    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        final UserInfo userInfo = (UserInfo) principals.getPrimaryPrincipal();
        final SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        final Set<Long> roles = this.userRoleService.findRolesByUid(userInfo.getId());
        final Set<String> permissions = this.roleMenuService.findMenusByUid(userInfo.getId());
        final Set<String> rolesConvert = new HashSet<>();

        if (roles != null && roles.size() > 0) {
            roles.forEach(role -> {
                rolesConvert.add(String.valueOf(role));
            });
        }

        info.setRoles(rolesConvert);
        info.setStringPermissions(permissions);
        return info;
    }
}
