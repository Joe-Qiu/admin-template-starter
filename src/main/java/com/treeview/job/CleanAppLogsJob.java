package com.treeview.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.treeview.entity.monitor.AppLogs;
import com.treeview.mapper.monitor.AppLogsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Slf4j
@Component
public class CleanAppLogsJob {
    @Resource
    private AppLogsMapper appLogsMapper;

    @Scheduled(cron = "0 0 0/8 * * ?")
    public void execute() {
        final QueryWrapper<AppLogs> ew = new QueryWrapper();
        final LocalDateTime dateTime = LocalDateTime.now();
        ew.le("create_time", dateTime.minusMonths(3).toLocalDate());
        this.appLogsMapper.delete(ew);
    }
}
