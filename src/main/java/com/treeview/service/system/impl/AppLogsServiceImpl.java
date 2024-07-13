package com.treeview.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.treeview.entity.monitor.AppLogs;
import com.treeview.mapper.monitor.AppLogsMapper;
import com.treeview.service.system.AppLogsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service("appLogsService")
public class AppLogsServiceImpl extends ServiceImpl<AppLogsMapper, AppLogs> implements AppLogsService {
    public void insertLog(String details, String uname, String url, String parms) {
        final AppLogs appLogs = new AppLogs();
        appLogs.setCreateTime(new Date());
        appLogs.setDetails(details);
        appLogs.setUsername(uname);
        appLogs.setUrl(url);
        appLogs.setParams(parms);
        super.saveOrUpdate(appLogs);
    }
}