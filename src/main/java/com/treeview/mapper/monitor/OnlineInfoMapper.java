package com.treeview.mapper.monitor;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.treeview.entity.monitor.OnlineInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

public interface OnlineInfoMapper extends BaseMapper<OnlineInfo> {

    @Select({"<script>" +
            "SELECT id, " +
            "       user_name, " +
            "       session, " +
            "       ip_addr, " +
            "       browser, " +
            "       os_name, " +
            "       last_login_time, " +
            "       last_visit_time " +
            "FROM (SELECT id, " +
            "             user_name, " +
            "             session, " +
            "             ip_addr, " +
            "             browser, " +
            "             os_name, " +
            "             last_login_time, " +
            "             last_visit_time, " +
            "             ROW_NUMBER() OVER (PARTITION BY user_name ORDER BY last_visit_time DESC) AS rn " +
            "      FROM online_info " +
            "      WHERE DATE(last_login_time) = CURDATE()) t " +
            "WHERE t.rn = 1 " +
            "       <if test=\"search != null\">" +
            "           AND t.user_name LIKE CONCAT('%',#{search},'%') " +
            "       </if>" +
            "</script>"})
    @ResultType(OnlineInfo.class)
    Page<OnlineInfo> getOnlineUsers(Page<OnlineInfo> page, @Param("search") String search);
}