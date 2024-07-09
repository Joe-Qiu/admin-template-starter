package com.treeview.entity.framework;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author joe.qiu on 2020/03/06
 * @version V1.0.0
 */
@Data
public class MyPage<T> implements Serializable {
    private static final long serialVersionUID = 298376954284564537L;

    private List<T> records;

    private long total;

    private long size;

    private long current;

    private long pages;
}