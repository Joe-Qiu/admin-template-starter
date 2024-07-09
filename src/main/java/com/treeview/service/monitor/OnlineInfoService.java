package com.treeview.service.monitor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.treeview.entity.monitor.OnlineInfo;

import java.util.Date;

public interface OnlineInfoService extends IService<OnlineInfo> {
    void updateUserLatestVisitTime(String sessionId, Date visitTime);

    /** 获取今日登录用户 **/
    Page<OnlineInfo> getOnlineUsers(Page<OnlineInfo> page, String search);
}
