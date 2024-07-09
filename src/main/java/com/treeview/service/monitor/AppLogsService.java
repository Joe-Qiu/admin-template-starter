package com.treeview.service.monitor;

import com.baomidou.mybatisplus.extension.service.IService;
import com.treeview.entity.monitor.AppLogs;

public interface AppLogsService extends IService<AppLogs> {
    void insertLog(String details, String uname, String url, String parms);
}
