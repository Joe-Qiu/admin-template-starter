package com.treeview.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.treeview.entity.system.UserRole;

import java.util.Set;

public interface UserRoleService extends IService<UserRole> {
    Set<Long> findRolesByUid(Long uuid);
}