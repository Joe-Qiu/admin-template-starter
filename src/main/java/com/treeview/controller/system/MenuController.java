package com.treeview.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.treeview.annotation.Logit;
import com.treeview.controller.base.SuperController;
import com.treeview.entity.framework.MyPage;
import com.treeview.entity.framework.Rest;
import com.treeview.entity.system.MenuConfig;
import com.treeview.service.system.MenuConfigService;
import org.apache.commons.collections4.CollectionUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping({"/system/menu"})
public class MenuController extends SuperController {
    @Resource
    private MenuConfigService menuConfigService;

    @RequiresPermissions({"listMenu"})
    @RequestMapping({"/list/{pageNumber}"})
    public String list(@PathVariable Integer pageNumber, @RequestParam(defaultValue = "10") Integer pageSize, String search, Model model) {
        Page<MenuConfig> page = this.getPage(pageNumber, pageSize);
        OrderItem orderItem = new OrderItem();
        orderItem.setColumn("code");
        orderItem.setAsc(true);
        page.addOrder(orderItem);
        model.addAttribute("pageSize", pageSize);
        QueryWrapper<MenuConfig> ew = new QueryWrapper<>();
        if (StringUtils.isNotBlank(search)) {
            ew.like("name", search);
            model.addAttribute("search", search);
        }

        Page<MenuConfig> pageData = this.menuConfigService.page(page, ew);

        if (CollectionUtils.isNotEmpty(pageData.getRecords())) {
            pageData.getRecords().forEach(menu -> {
                if (menu.getPid() != null && menu.getDeep() == 3) {
                    menu.setName(StringUtils.join("<i class='fa fa-file'></i> ", menu.getName()));
                } else {
                    menu.setName(StringUtils.join("<i class='fa fa-folder-open'></i> ", menu.getName()));
                }

                for (int i = 1; i < menu.getDeep(); ++i) {
                    menu.setName(StringUtils.join("&nbsp;&nbsp;", menu.getName()));
                }
            });
        }

        MyPage<MenuConfig> myPage = new MyPage<>();
        BeanUtils.copyProperties(pageData, myPage);
        model.addAttribute("pageData", myPage);
        return "system/menu/list";
    }

    @RequiresPermissions({"addMenu"})
    @RequestMapping({"/add"})
    public String add(Model model) {
        QueryWrapper<MenuConfig> ew = new QueryWrapper<>();
        ew.eq("pid", "0");
        ew.orderByAsc("code");

        final List<MenuConfig> list = this.menuConfigService.list(ew);
        model.addAttribute("list", list);

        ew = new QueryWrapper<>();
        ew.eq("deep", "2");
        ew.orderByAsc("code");

        final List<MenuConfig> subList = this.menuConfigService.list(ew);
        model.addAttribute("subList", subList);

        return "system/menu/add";
    }

    @RequiresPermissions({"addMenu"})
    @Logit("创建目录菜单")
    @RequestMapping({"/doAddDir"})
    @ResponseBody
    public Rest doAddDir(MenuConfig menuConfig, Model model) {
        menuConfig.setPid("0");
        menuConfig.setDeep(1);
        this.menuConfigService.save(menuConfig);
        return Rest.ok();
    }

    @RequiresPermissions({"addMenu"})
    @Logit("创建菜单")
    @RequestMapping({"/doAddMenu"})
    @ResponseBody
    public Rest doAddMenu(MenuConfig menuConfig, Model model) {
        menuConfig.setDeep(2);
        this.menuConfigService.save(menuConfig);
        return Rest.ok();
    }

    @RequiresPermissions({"editMenu"})
    @RequestMapping({"/edit/{id}"})
    public String edit(@PathVariable String id, Model model) {
        final MenuConfig menuConfig = this.menuConfigService.getById(id);
        model.addAttribute("menuConfig", menuConfig);
        if (menuConfig.getDeep() == 1) {
            return "/system/menu/edit";
        } else {
            QueryWrapper<MenuConfig> ew = null;
            List<MenuConfig> list = null;
            if (menuConfig.getDeep() == 2) {
                ew = new QueryWrapper<>();
                ew.orderBy(false, true, "code");
                ew.eq("pid", "0");
                list = this.menuConfigService.list(ew);
                model.addAttribute("list", list);
                return "/system/menu/editMenu";
            } else {
                ew = new QueryWrapper<>();
                ew.orderBy(false, true, "code");
                ew.eq("pid", "0");
                list = this.menuConfigService.list(ew);
                model.addAttribute("list", list);
                model.addAttribute("pSysMenu", this.menuConfigService.getById(menuConfig.getPid()));
                return "/system/menu/editAction";
            }
        }
    }

    @RequiresPermissions({"editMenu"})
    @Logit("编辑菜单")
    @RequestMapping({"/doEdit"})
    @ResponseBody
    public Rest doEdit(MenuConfig menuConfig) {
        this.menuConfigService.updateById(menuConfig);
        return Rest.ok();
    }

    @RequiresPermissions({"deleteMenu"})
    @Logit("删除菜单")
    @RequestMapping({"/delete"})
    @ResponseBody
    public Rest delete(Long id) {
        this.menuConfigService.removeById(id);
        return Rest.ok();
    }

    @RequestMapping({"/json"})
    @ResponseBody
    public Rest json(String pid) {
        QueryWrapper<MenuConfig> ew = new QueryWrapper<>();
        ew.eq("pid", pid);
        ew.orderByAsc("sort");
        List<MenuConfig> menuConfigs = this.menuConfigService.list(ew);
        List<Map<String, Object>> listMap = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(menuConfigs)) {
            menuConfigs.forEach(menuConfig -> {
                Map<String, Object> map = new HashMap<>(16);
                map.put("id", menuConfig.getId());
                map.put("text", StringUtils.join(menuConfig.getCode(), "-", menuConfig.getName()));
                listMap.add(map);
            });
        }

        return Rest.okData(listMap);
    }

    @RequestMapping({"/checkMenuResource"})
    @ResponseBody
    public Rest checkMenuResource(String resource) {
        final QueryWrapper<MenuConfig> ew = new QueryWrapper<>();
        ew.eq("resource", resource);

        final List<MenuConfig> menuConfigs = this.menuConfigService.list();
        return !menuConfigs.isEmpty() ? Rest.failure("资源已存在,请换一个尝试.") : Rest.ok();
    }

    @RequiresPermissions({"addMenu"})
    @Logit("新增功能菜单")
    @RequestMapping({"/doAddAction"})
    @ResponseBody
    public Rest doAddAction(MenuConfig menuConfig, Model model) {
        menuConfig.setDeep(3);
        this.menuConfigService.save(menuConfig);
        return Rest.ok();
    }
}
