package com.treeview.entity.system;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class TreeMenu implements Serializable {
    private static final long serialVersionUID = -8799872378091432575L;

    private MenuConfig menuConfig;

    private List<TreeMenu> children = new ArrayList<>();
}