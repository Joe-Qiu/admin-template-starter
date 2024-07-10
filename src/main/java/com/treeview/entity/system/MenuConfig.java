package com.treeview.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("menu_config")
public class MenuConfig extends Model<MenuConfig> implements Serializable{
    private static final long serialVersionUID = 536909620890199438L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("pid")
    private String pid;

    @TableField("url")
    private String url;

    @TableField("icon")
    private String icon;

    @TableField("sort")
    private Integer sort;

    @TableField("deep")
    private Integer deep;

    @TableField("code")
    private String code;

    @TableField("resource")
    private String resource;
}