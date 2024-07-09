package com.treeview.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.treeview.annotation.Logit;
import com.treeview.controller.base.SuperController;
import com.treeview.entity.framework.MyPage;
import com.treeview.entity.framework.Rest;
import com.treeview.entity.system.RoleDefine;
import com.treeview.entity.system.UserInfo;
import com.treeview.entity.system.UserRole;
import com.treeview.service.system.DepartmentService;
import com.treeview.service.system.RoleDefineService;
import com.treeview.service.system.UserInfoService;
import com.treeview.service.system.UserRoleService;
import com.treeview.utils.ShiroUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping({"/system/user"})
public class UserController extends SuperController {
    @Resource
    private UserInfoService userInfoService;

    @Resource
    private RoleDefineService roleDefineService;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private DepartmentService departmentService;

    @RequiresPermissions({"listUser"})
    @RequestMapping(value = {"/list/{pageNumber}"})
    public String list(@PathVariable Integer pageNumber, @RequestParam(defaultValue = "10") Integer pageSize, String search, Model model) {
        if (StringUtils.isNotBlank(search)) {
            model.addAttribute("search", search);
        }

        Page<Map<Object, Object>> page = this.getPage(pageNumber, pageSize);
        model.addAttribute("pageSize", pageSize);
        Page<Map<Object, Object>> pageData = this.userInfoService.selectUserPage(page, search);

        MyPage<Map<Object, Object>> myPage = new MyPage();
        BeanUtils.copyProperties(pageData, myPage);
        model.addAttribute("pageData", myPage);

        log.debug(gson.toJson(myPage));
        return "system/user/list";
    }

    @RequiresPermissions({"addUser"})
    @RequestMapping({"/add"})
    public String add(Model model) {
        model.addAttribute("roleList", this.roleDefineService.list());
        model.addAttribute("deptList", this.departmentService.list());
        return "system/user/add";
    }

    @Logit("创建用户")
    @RequiresPermissions({"addUser"})
    @RequestMapping({"/doAdd"})
    @ResponseBody
    public Rest doAdd(UserInfo userInfo, @RequestParam(value = "roleIds", required = false) List<Long> roleIds) {
        try {
            if (userInfo != null && StringUtils.isNotEmpty(userInfo.getUserName())) {
                userInfo.setUserName(userInfo.getUserName());
                userInfo.setPassWord("123456");
            }

            if(roleIds != null && roleIds.size() > 0){
                userInfoService.insertUser(userInfo, roleIds.stream().toArray(Long[]::new));
            }else{
                userInfoService.insertUser(userInfo, null);
            }
            return Rest.ok();
        } catch (Exception e) {
            log.error("Exception", e);
            return Rest.failure("创建用户失败");
        }
    }

    @Logit("删除用户")
    @RequiresPermissions({"deleteUser"})
    @RequestMapping({"/delete"})
    @ResponseBody
    public Rest delete(Long id) {
        userInfoService.deleteUser(id);
        return Rest.ok();
    }

    @RequestMapping({"/edit/{id}"})
    @RequiresPermissions({"editUser"})
    public String edit(@PathVariable String id, Model model) {
        final UserInfo userInfo = this.userInfoService.getById(id);
        final List<RoleDefine> roleDefines = this.roleDefineService.list();

        final QueryWrapper<UserRole> ew = new QueryWrapper();
        ew.eq("user_id ", id);

        final List<UserRole> myUserRoles = this.userRoleService.list(ew);
        final StringBuffer myRoles = new StringBuffer();
        if (myUserRoles != null && myUserRoles.size() > 0) {
            myUserRoles.forEach((item) -> {
                myRoles.append(item.getRoleId().toString()).append(",");
            });
        }

        model.addAttribute("userInfo", userInfo);
        model.addAttribute("roles", roleDefines);
        model.addAttribute("myRoles", myRoles);
        model.addAttribute("deptList", this.departmentService.list());
        return "system/user/edit";
    }

    @Logit("编辑用户")
    @RequiresPermissions({"editUser"})
    @RequestMapping({"/doEdit"})
    @ResponseBody
    public Rest doEdit(UserInfo userInfo, @RequestParam(value = "roleId[]", required = false) Long[] roleId, Model model) {
        this.userInfoService.updateUser(userInfo, roleId);
        return Rest.ok();
    }

    @RequestMapping({"/checkName"})
    @ResponseBody
    public Rest checkName(String username) {
        final QueryWrapper<UserInfo> ew = new QueryWrapper<>();
        ew.eq("user_name", username);
        final List<UserInfo> list = this.userInfoService.list(ew);

        return list.size() > 0 ? Rest.failure("用户名已存在") : Rest.ok();
    }

    @RequestMapping(value = {"/showProfile"})
    public String showProfile(Model model) {
        UserInfo userInfo = ShiroUtil.getSessionUser();

        final QueryWrapper<UserInfo> ew = new QueryWrapper<>();
        ew.eq("user_name", userInfo.getUserName());
        userInfo = userInfoService.getOne(ew);

        model.addAttribute("userInfo", userInfo);

        return "system/user/showProfile";
    }

    @PostMapping(value = {"/saveProfile"})
    @ResponseBody
    public Rest saveProfile(@Validated UserInfo userInfo) {
        userInfoService.updateById(userInfo);
        return Rest.ok("资料更新成功");
    }

    @PostMapping(value = {"/resetPassword"})
    @ResponseBody
    public Rest resetPassword(Long id, String oldPassWord, String newPassWord) {
        final UserInfo userInfo = userInfoService.getById(id);

        if(userInfo == null){
            return Rest.failure("用户不存在");
        }

        if(StringUtils.isEmpty(oldPassWord)){
            return Rest.failure("老密码不能为空");
        }

        if (StringUtils.isEmpty(newPassWord)){
            return Rest.failure("新密码不能为空");
        }

        if(StringUtils.equals(newPassWord, oldPassWord)){
            return Rest.failure("新密码不能与老密码相同");
        }

        if(!userInfo.getPassWord().equals(userInfoService.encryptPassWord(userInfo.getUserName(), oldPassWord, userInfo.getSalt()))){
            return Rest.failure("您输入的老密码不正确");
        }

        final String encryptPassWord = userInfoService.encryptPassWord(userInfo.getUserName(), newPassWord, userInfo.getSalt());

        userInfo.setPassWord(encryptPassWord);
        userInfo.setUpdateTime(new Date());
        userInfoService.updateById(userInfo);

        return Rest.ok("密码重设成功，请重新登录");
    }

    @RequestMapping({"/resetPassWord/{id}"})
    public String resetPassWord(@PathVariable Long id, Model model) {
        final UserInfo userInfo = this.userInfoService.getById(id);

        model.addAttribute("userInfo", userInfo);
        return "system/user/resetPassWord";
    }
}