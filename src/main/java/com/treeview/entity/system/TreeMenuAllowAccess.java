package com.treeview.entity.system;

import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class TreeMenuAllowAccess implements Serializable {
    private static final long serialVersionUID = 5386241934890610433L;

    private MenuConfig menuConfig;

    private boolean allowAccess = false;

    private List<TreeMenuAllowAccess> children = Lists.newArrayList();
}
