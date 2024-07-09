package com.treeview.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.treeview.entity.system.RoleMenu;
import com.treeview.mapper.system.MenuConfigMapper;
import com.treeview.mapper.system.RoleMenuMapper;
import com.treeview.service.system.RoleMenuService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {
    @Resource
    private MenuConfigMapper menuConfigMapper;

    public void addAuth(Long roleId, Long[] menuIds) {
        this.remove((Wrapper)(new QueryWrapper()).eq("role_id", roleId));
        if (ArrayUtils.isNotEmpty(menuIds)) {
            for (Long menuId : menuIds){
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                this.save(roleMenu);
            }
        }
    }

    public List<RoleMenu> selectByRole(Long roleId) {
        final QueryWrapper<RoleMenu> ew = new QueryWrapper();
        ew.eq("role_id", roleId);
        return this.list(ew);
    }

    public Set<String> findMenusByUid(Long id) {
        final List<String> list = this.menuConfigMapper.selectResourceByUid(id);
        return new HashSet(list);
    }
}