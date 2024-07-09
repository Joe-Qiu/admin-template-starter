package com.treeview.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.treeview.entity.system.UserRole;
import com.treeview.mapper.system.UserRoleMapper;
import com.treeview.service.system.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
    public Set<Long> findRolesByUid(Long uid) {
        final List<UserRole> userRoles = this.list((Wrapper) (new QueryWrapper()).eq("user_id", uid));
        final Set<Long> set = new HashSet();

        if (userRoles != null && userRoles.size() > 0) {
            userRoles.forEach(employeeRole -> {
                set.add(employeeRole.getRoleId());
            });
        }

        return set;
    }
}