package com.treeview.service.monitor.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.treeview.entity.monitor.OnlineInfo;
import com.treeview.mapper.monitor.OnlineInfoMapper;
import com.treeview.service.monitor.OnlineInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service("onlineInfoService")
public class OnlineInfoServiceImpl extends ServiceImpl<OnlineInfoMapper, OnlineInfo> implements OnlineInfoService {
    @Override
    public void updateUserLatestVisitTime(String sessionId, Date visitTime){
        final UpdateWrapper<OnlineInfo> ew = new UpdateWrapper<>();
        ew.eq("session", sessionId).set("last_visit_time", visitTime);

        this.update(ew);
    }

    @Override
    public Page<OnlineInfo> getOnlineUsers(Page<OnlineInfo> page, String search) {
        return this.getBaseMapper().getOnlineUsers(page, search);
    }
}
