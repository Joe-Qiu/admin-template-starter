package com.treeview.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.treeview.controller.base.SuperController;
import com.treeview.entity.framework.MyPage;
import com.treeview.entity.monitor.AppLogs;
import com.treeview.service.monitor.AppLogsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
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
        Page<AppLogs> page = this.getPage(pageNumber, pageSize);
        OrderItem orderItem = new OrderItem();
        orderItem.setColumn("create_time");
        orderItem.setAsc(false);
        page.addOrder(new OrderItem[]{orderItem});
        model.addAttribute("pageSize", pageSize);
        QueryWrapper<AppLogs> ew = new QueryWrapper();
        if (StringUtils.isNotBlank(search)) {
            ((QueryWrapper)((QueryWrapper)ew.like("username", search)).or()).like("details", search);
            model.addAttribute("search", search);
        }

        if (StringUtils.isNotBlank(daterange)) {
            model.addAttribute("daterange", daterange);
            String[] dateranges = StringUtils.split(daterange, "-");
            ew.ge("create_time", dateranges[0].trim().replaceAll("/", "-") + " 00:00:00");
            ew.le("create_time", dateranges[1].trim().replaceAll("/", "-") + " 23:59:59");
        }

        Page<AppLogs> pageData = this.appLogsService.page(page, ew);
        MyPage<AppLogs> myPage = new MyPage();
        BeanUtils.copyProperties(pageData, myPage);
        model.addAttribute("pageData", myPage);
        return "system/log/list";
    }

    @RequestMapping({"/params/{id}"})
    @ResponseBody
    public String params(@PathVariable String id, Model model) {
        AppLogs appLogs = (AppLogs)this.appLogsService.getById(id);
        return appLogs.getParams();
    }
}
