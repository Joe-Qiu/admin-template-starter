package com.treeview.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.treeview.entity.system.MenuConfig;
import com.treeview.entity.system.TreeMenu;
import com.treeview.entity.system.TreeMenuAllowAccess;

import java.util.List;

public interface MenuConfigService extends IService<MenuConfig> {
    List<String> selectMenuIdsByUserId(Long uid);

    List<TreeMenu> selectTreeMenuByMenuIdsAndPid(List<Long> menuIds, Long pid);

    List<TreeMenu> selectTreeMenuByUserId(Long uid);

    List<TreeMenuAllowAccess> selectTreeMenuAllowAccessByMenuIdsAndPid(List<Long> menuIds, Long pid);
}
