package com.treeview.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.treeview.annotation.Logit;
import com.treeview.controller.basic.SuperController;
import com.treeview.entity.framework.Rest;
import com.treeview.entity.system.Department;
import com.treeview.service.system.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Controller
@RequestMapping({"/system/dept"})
public class DeptController extends SuperController {
    @Resource
    private DepartmentService departmentService;

    @RequiresPermissions({"listDept"})
    @RequestMapping({"/list/{pageNumber}"})
    public String list(@PathVariable Integer pageNumber, @RequestParam(defaultValue = "10") Integer pageSize, String search, Model model) {
        final Page<Department> page = this.getPage(pageNumber, pageSize);
        model.addAttribute("pageSize", pageSize);

        final QueryWrapper<Department> ew = new QueryWrapper();
        if (StringUtils.isNotBlank(search)) {
            ew.like("dept_name", search);
            model.addAttribute("search", search);
        }

        Page<Department> pageData = this.departmentService.page(page, ew);
        model.addAttribute("pageData", pageData);
        return "system/dept/list";
    }

    @RequiresPermissions({"addDept"})
    @RequestMapping({"/add"})
    public String add(Model model) {
        return "system/dept/add";
    }

    @RequiresPermissions({"addDept"})
    @Logit("创建部门")
    @RequestMapping({"/doAdd"})
    @ResponseBody
    public Rest doAdd(Department dept) {
        this.departmentService.save(dept);
        return Rest.ok();
    }

    @RequiresPermissions({"deleteDept"})
    @Logit("删除部门")
    @RequestMapping({"/delete"})
    @ResponseBody
    public Rest delete(String id) {
        this.departmentService.removeById(id);
        return Rest.ok();
    }

    @RequiresPermissions({"editDept"})
    @RequestMapping({"/edit/{id}"})
    public String edit(@PathVariable String id, Model model) {
        final Department dept = this.departmentService.getById(id);
        model.addAttribute("dept", dept);
        return "system/dept/edit";
    }

    @RequiresPermissions({"editDept"})
    @Logit("编辑部门")
    @RequestMapping({"/doEdit"})
    @ResponseBody
    public Rest doEdit(Department dept, Model model) {
        this.departmentService.updateById(dept);
        return Rest.ok();
    }

    @RequestMapping({"/checkDept"})
    @ResponseBody
    public Rest checkDept(String deptName) {
        final QueryWrapper<Department> ew = new QueryWrapper<>();
        ew.eq("dept_name", deptName);

        final List<Department> list = this.departmentService.list(ew);
        return list.size() > 0 ? Rest.failure("部门名已存在") : Rest.ok();
    }
}