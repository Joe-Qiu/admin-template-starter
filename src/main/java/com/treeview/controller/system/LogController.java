package com.treeview.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.treeview.controller.basic.SuperController;
import com.treeview.entity.monitor.AppLogs;
import com.treeview.service.system.AppLogsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping({"/system/log"})
public class LogController extends SuperController {
    @Resource
    private AppLogsService appLogsService;

    @RequiresPermissions({"listLog"})
    @RequestMapping({"/list/{pageNumber}"})
    public String list(@PathVariable Integer pageNumber, @RequestParam(defaultValue = "10") Integer pageSize, String search, String daterange, Model model) {
        final Page<AppLogs> page = this.getPage(pageNumber, pageSize);
        model.addAttribute("pageSize", pageSize);

        final QueryWrapper<AppLogs> ew = new QueryWrapper<>();
        ew.orderByDesc("create_time");

        if (StringUtils.isNotBlank(search)) {
            ew.like("username", search);
            ew.or();
            ew.like("details", search);

            model.addAttribute("search", search);
        }

        if (StringUtils.isNotBlank(daterange)) {
            model.addAttribute("daterange", daterange);
            String[] dateRanges = StringUtils.split(daterange, "-");
            ew.ge("create_time", dateRanges[0].trim().replaceAll("/", "-") + " 00:00:00");
            ew.le("create_time", dateRanges[1].trim().replaceAll("/", "-") + " 23:59:59");
        }

        final Page<AppLogs> pageData = this.appLogsService.page(page, ew);
        model.addAttribute("pageData", pageData);
        return "system/log/list";
    }

    @RequestMapping({"/params/{id}"})
    @ResponseBody
    public String params(@PathVariable String id, Model model) {
        AppLogs appLogs = this.appLogsService.getById(id);
        return appLogs.getParams();
    }
}