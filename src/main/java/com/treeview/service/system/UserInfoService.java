package com.treeview.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.treeview.entity.system.UserInfo;

import java.util.Map;

public interface UserInfoService extends IService<UserInfo> {
    Page<Map<Object, Object>> selectUserPage(Page<Map<Object, Object>> var1, String var2);

    void insertUser(UserInfo userInfo, Long[] roleIds);

    void updateUser(UserInfo userInfo, Long[] roleIds);

    void deleteUser(Long id);

    boolean saveUser(UserInfo userInfo);

    String encryptPassWord(String username, String password, String salt);

    String getSalt();
}
