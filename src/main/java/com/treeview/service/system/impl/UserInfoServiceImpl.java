package com.treeview.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.treeview.entity.system.UserInfo;
import com.treeview.entity.system.UserRole;
import com.treeview.event.UserRegistrationEvent;
import com.treeview.mapper.system.UserInfoMapper;
import com.treeview.mapper.system.UserRoleMapper;
import com.treeview.service.system.UserInfoService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Resource
    private UserInfoMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private ApplicationContext applicationContext;

    public void insertUser(UserInfo userInfo, Long[] roleIds) {
        userInfo.setCreateTime(new Date());
        this.userMapper.insert(userInfo);
        if (roleIds != null) {
            this.insertRoleId(userInfo, roleIds);
        }
    }

    public void updateUser(UserInfo userInfo, Long[] roleIds) {
        this.userMapper.updateById(userInfo);

        final QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userInfo.getId());
        this.userRoleMapper.delete(queryWrapper);

        this.insertRoleId(userInfo, roleIds);
    }

    private void insertRoleId(UserInfo userInfo, Long[] roleIds) {
        if (ArrayUtils.isNotEmpty(roleIds)) {
            Arrays.stream(roleIds).forEach((rid) -> {
                UserRole userRole = new UserRole();
                userRole.setUserId(userInfo.getId());
                userRole.setRoleId(rid);
                this.userRoleMapper.insert(userRole);
            });
        }
    }

    public Page<Map<Object, Object>> selectUserPage(Page<Map<Object, Object>> page, String search) {
        page.setRecords(this.baseMapper.selectUserList(page, search));
        return page;
    }

    public void deleteUser(Long id) {
        this.userMapper.deleteById(id);
        this.userRoleMapper.delete((Wrapper) (new QueryWrapper()).eq("user_id", id));
    }

    @Override
    public boolean saveUser(UserInfo userInfo) {
        String userName = userInfo.getUserName();
        String passWord = userInfo.getPassWord();

        userInfo.setCreateTime(new Date());
        userInfo.setSalt(this.getSalt());
        userInfo.setPassWord(this.encryptPassWord(userName, passWord, userInfo.getSalt()));

        this.save(userInfo);

        applicationContext.publishEvent(new UserRegistrationEvent(this, userInfo));
        return true;
    }

    @Override
    public String encryptPassWord(String username, String password, String salt) {
        if(StringUtils.isEmpty(salt)){
            salt = getSalt();
        }

        return new Md5Hash(username + password + salt).toHex();
    }
    @Override
    public String getSalt() {
        return RandomStringUtils.randomAlphabetic(20);
    }
}