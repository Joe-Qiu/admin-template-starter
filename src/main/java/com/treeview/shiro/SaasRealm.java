package com.treeview.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.treeview.entity.system.UserInfo;
import com.treeview.enums.system.user.UserStatusEnum;
import com.treeview.service.system.RoleMenuService;
import com.treeview.service.system.UserInfoService;
import com.treeview.service.system.UserRoleService;
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
