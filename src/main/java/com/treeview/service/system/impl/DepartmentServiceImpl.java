package com.treeview.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.treeview.entity.system.Department;
import com.treeview.mapper.system.DepartmentMapper;
import com.treeview.service.system.DepartmentService;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {
}
