package com.treeview.controller.system;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.treeview.annotation.Logit;
import com.treeview.controller.basic.SuperController;
import com.treeview.entity.framework.Rest;
import com.treeview.entity.system.*;
import com.treeview.service.system.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping({"/system/role"})
public class RoleController extends SuperController {
    @Resource
    private RoleDefineService roleDefineService;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private MenuConfigService menuConfigService;
    @Resource
    private RoleMenuService roleMenuService;

    @RequiresPermissions({"listRole"})
    @RequestMapping({"/list/{pageNumber}"})
    public String list(@PathVariable Integer pageNumber, @RequestParam(defaultValue = "10") Integer pageSize, String search, Model model) {
        final Page<RoleDefine> page = this.getPage(pageNumber, pageSize);

        model.addAttribute("pageSize", pageSize);
        final QueryWrapper<RoleDefine> ew = new QueryWrapper();
        if (StringUtils.isNotBlank(search)) {
            ew.like("role_name", search);
            ew.orderByDesc("create_time");
            model.addAttribute("search", search);
        }

        Page<RoleDefine> pageData = this.roleDefineService.page(page, ew);
        model.addAttribute("pageData", pageData);
        return "system/role/list";
    }

    @RequiresPermissions({"addRole"})
    @RequestMapping({"/add"})
    public String add(Model model) {
        return "system/role/add";
    }

    @RequiresPermissions({"addRole"})
    @Logit("创建角色")
    @RequestMapping({"/doAdd"})
    @ResponseBody
    public Rest doAdd(RoleDefine role) {
        role.setCreateTime(new Date());
        this.roleDefineService.save(role);
        return Rest.ok();
    }

    @RequiresPermissions({"deleteRole"})
    @Logit("删除角色")
    @RequestMapping({"/delete"})
    @ResponseBody
    public Rest delete(String id) {
        this.roleDefineService.removeById(id);
        return Rest.ok();
    }

    @RequiresPermissions({"deleteBatchRole"})
    @Logit("批量删除角色")
    @RequestMapping({"/deleteBatch"})
    @ResponseBody
    public Rest deleteBatch(@RequestParam("id[]") List<String> ids) {
        this.roleDefineService.removeByIds(ids);
        return Rest.ok();
    }

    @RequiresPermissions({"editRole"})
    @RequestMapping({"/edit/{id}"})
    public String edit(@PathVariable String id, Model model) {
        RoleDefine roleDefine = this.roleDefineService.getById(id);
        model.addAttribute(roleDefine);
        return "system/role/edit";
    }

    @RequiresPermissions({"editRole"})
    @Logit("编辑角色")
    @RequestMapping({"/doEdit"})
    @ResponseBody
    public Rest doEdit(RoleDefine roleDefine, Model model) {
        this.roleDefineService.updateById(roleDefine);
        return Rest.ok();
    }

    @RequiresPermissions({"authRole"})
    @RequestMapping({"/auth/{id}"})
    public String auth(@PathVariable String id, Model model) {
        final RoleDefine roleDefine = this.roleDefineService.getById(id);
        if (roleDefine == null) {
            throw new RuntimeException("该角色不存在");
        } else {
            final QueryWrapper<RoleMenu> ew = new QueryWrapper();
            ew.eq("role_id", id);

            final List<RoleMenu> roleMenus = this.roleMenuService.list(ew);
            final List<Long> menuIds = roleMenus.stream().map((input) -> input.getMenuId()).collect(Collectors.toList());
            final List<TreeMenuAllowAccess> treeMenuAllowAccesses = this.menuConfigService.selectTreeMenuAllowAccessByMenuIdsAndPid(menuIds, 0L);

            model.addAttribute("roleDefine", roleDefine);
            model.addAttribute("treeMenuAllowAccesses", treeMenuAllowAccesses);

            return "system/role/auth";
        }
    }

    @RequiresPermissions({"authRole"})
    @Logit("角色分配权限")
    @RequestMapping({"/doAuth"})
    @ResponseBody
    public Rest doAuth(Long roleId, @RequestParam(value = "mid[]",required = false) Long[] mid) {
        this.roleMenuService.addAuth(roleId, mid);
        return Rest.ok("OK,授权成功,1分钟后生效  ~");
    }

    @RequestMapping({"/getUsers"})
    public String getUsers(String roleId, Model model) {
        final QueryWrapper<UserRole> ew = new QueryWrapper();
        ew.eq("role_id", roleId);

        final List<UserRole> userRoles = this.userRoleService.list(ew);
        final List<Long> userIds = userRoles.stream().map((input) -> input.getUserId()).collect(Collectors.toList());

        List<UserInfo> users = new ArrayList();
        if (userIds.size() > 0) {
            QueryWrapper<UserInfo> ewUser = new QueryWrapper();
            ewUser.in("id", userIds);
            users = this.userInfoService.list(ewUser);
        }

        model.addAttribute("users", users);
        return "system/role/users";
    }

    @RequestMapping({"/getCount"})
    @ResponseBody
    public String getCount(String roleId) {
        long count = this.userRoleService.count((Wrapper)(new QueryWrapper()).eq("role_id", roleId));
        return String.valueOf(count);
    }
}