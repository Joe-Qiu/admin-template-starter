package com.treeview.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.treeview.entity.system.RoleDefine;
import com.treeview.mapper.system.RoleDefineMapper;
import com.treeview.service.system.RoleDefineService;
import org.springframework.stereotype.Service;

@Service
public class RoleDefineServiceImpl extends ServiceImpl<RoleDefineMapper, RoleDefine> implements RoleDefineService {
}
