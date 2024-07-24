package com.treeview.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.treeview.entity.system.MenuConfig;
import com.treeview.entity.system.TreeMenu;
import com.treeview.entity.system.TreeMenuAllowAccess;
import com.treeview.mapper.system.MenuConfigMapper;
import com.treeview.mapper.system.RoleMenuMapper;
import com.treeview.service.system.MenuConfigService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service()
public class MenuConfigServiceImpl extends ServiceImpl<MenuConfigMapper, MenuConfig> implements MenuConfigService {
    @Resource
    private MenuConfigMapper menuConfigMapper;
    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Cacheable(value = {"permissionCache"},key = "'permission_' + #uid")
    public List<String> selectMenuIdsByUserId(Long uid) {
        return this.menuConfigMapper.selectMenuIdsByUserId(uid);
    }

    @Cacheable(value = {"menuCache"},key = "'menu_' + #uid")
    public List<TreeMenu> selectTreeMenuByUserId(Long uid) {
        final List<Long> menuIds = this.roleMenuMapper.selectRoleMenuIdsByUserId(uid);
        return this.selectTreeMenuByMenuIdsAndPid(menuIds, 0L);
    }

    public List<TreeMenu> selectTreeMenuByMenuIdsAndPid(List<Long> menuIds, Long pid) {
        final QueryWrapper<MenuConfig> ew = new QueryWrapper<>();
        ew.eq("pid", pid);
        if(menuIds != null && !menuIds.isEmpty()){
            ew.in("id", menuIds);
        }
        ew.orderByAsc("sort");

        final List<MenuConfig> menuConfigs = this.list(ew);
        final List<TreeMenu> treeMenus = new ArrayList<>();

        TreeMenu treeMenu;
        if(menuConfigs != null && !menuConfigs.isEmpty()){
            for (MenuConfig menuConfig : menuConfigs) {
                treeMenu = new TreeMenu();
                treeMenu.setMenuConfig(menuConfig);
                if (menuConfig.getDeep() < 2) {
                    treeMenu.setChildren(this.selectTreeMenuByMenuIdsAndPid(menuIds, menuConfig.getId()));
                }
                treeMenus.add(treeMenu);
            }
        }

        return treeMenus;
    }

    public List<TreeMenuAllowAccess> selectTreeMenuAllowAccessByMenuIdsAndPid(List<Long> menuIds, Long pid) {
        final QueryWrapper<MenuConfig> ew = new QueryWrapper<>();
        ew.orderBy(false, true, "sort");
        ew.eq("pid", pid);

        final List<MenuConfig> menuConfigs = this.list(ew);
        final List<TreeMenuAllowAccess> treeMenuAllowAccesses = new ArrayList<>();

        TreeMenuAllowAccess treeMenuAllowAccess;
        if(menuConfigs != null && !menuConfigs.isEmpty()){
            for (MenuConfig menuConfig : menuConfigs) {
                treeMenuAllowAccess = new TreeMenuAllowAccess();
                treeMenuAllowAccess.setMenuConfig(menuConfig);
                if (menuIds.contains(menuConfig.getId())) {
                    treeMenuAllowAccess.setAllowAccess(true);
                }

                if (menuConfig.getDeep() < 3) {
                    treeMenuAllowAccess.setChildren(this.selectTreeMenuAllowAccessByMenuIdsAndPid(menuIds, menuConfig.getId()));
                }
                treeMenuAllowAccesses.add(treeMenuAllowAccess);
            }
        }

        return treeMenuAllowAccesses;
    }
}