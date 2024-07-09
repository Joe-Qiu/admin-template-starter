package com.treeview.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.treeview.entity.system.UserRole;

import java.util.List;

public interface UserRoleMapper extends BaseMapper<UserRole> {
    List<String> selectPermissionByUid(Long var1);
}