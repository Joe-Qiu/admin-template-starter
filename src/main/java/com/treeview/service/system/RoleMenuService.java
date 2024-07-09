package com.treeview.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.treeview.entity.system.RoleMenu;

import java.util.List;
import java.util.Set;

public interface RoleMenuService extends IService<RoleMenu> {
    void addAuth(Long roleId, Long[] menuIds);

    List<RoleMenu> selectByRole(Long roleId);

    Set<String> findMenusByUid(Long id);
}