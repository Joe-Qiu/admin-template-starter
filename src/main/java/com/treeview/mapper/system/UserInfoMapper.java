package com.treeview.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.treeview.entity.system.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserInfoMapper extends BaseMapper<UserInfo> {
    @Select({"<script>" +
            "   SELECT u.id, u.dept_id, u.user_name, u.status, DATE_FORMAT(u.create_time, '%Y-%m-%d %H:%i:%s') as create_time, DATE_FORMAT(u.update_time, '%Y-%m-%d %H:%i:%s') as update_time, d.dept_name, d.dept_desc FROM user_info u LEFT JOIN department d ON d.id = u.dept_id WHERE 1=1 " +
            "       <if test=\"search != null\">" +
            "           AND u.user_name LIKE CONCAT('%',#{search},'%') " +
            "       </if>" +
            "</script>"})
    @ResultType(HashMap.class)
    List<Map<Object, Object>> selectUserList(Page<Map<Object, Object>> page, @Param("search") String search);
}