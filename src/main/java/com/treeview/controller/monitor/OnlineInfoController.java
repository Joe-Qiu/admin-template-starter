package com.treeview.controller.monitor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.treeview.controller.base.SuperController;
import com.treeview.entity.framework.MyPage;
import com.treeview.entity.monitor.OnlineInfo;
import com.treeview.service.monitor.OnlineInfoService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Controller
@RequestMapping({"/monitor/online/"})
public class OnlineInfoController extends SuperController {
    @Resource
    private OnlineInfoService onlineInfoService;

    @RequiresPermissions({"listOnlineUsers"})
    @RequestMapping({"/list/{pageNumber}"})
    public String list(@PathVariable Integer pageNumber, @RequestParam(defaultValue = "10") Integer pageSize, String search, Model model) {
        final Page<OnlineInfo> page = this.getPage(pageNumber, pageSize);
        model.addAttribute("pageSize", pageSize);

        final Page<OnlineInfo> pageData = this.onlineInfoService.getOnlineUsers(page, search);

        if(CollectionUtils.isNotEmpty(pageData.getRecords())){
            pageData.getRecords().forEach(onlineInfo -> {
                final LocalDateTime localDateTime = LocalDateTime.now();
                final Date lastVisitTime = onlineInfo.getLastVisitTime();

                Instant instant = lastVisitTime.toInstant();
                ZoneId zoneId = ZoneId.systemDefault();
                LocalDateTime localVisitTime = instant.atZone(zoneId).toLocalDateTime();

                if(localDateTime.minusMinutes(10).isBefore(localVisitTime)){
                    onlineInfo.setStatus(1);
                }else{
                    onlineInfo.setStatus(0);
                }
            });
        }
        MyPage<OnlineInfo> myPage = new MyPage();
        BeanUtils.copyProperties(pageData, myPage);
        model.addAttribute("pageData", myPage);
        return "monitor/online/list";
    }
}