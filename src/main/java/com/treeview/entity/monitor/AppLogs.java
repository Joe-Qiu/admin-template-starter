package com.treeview.entity.monitor;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("app_logs")
public class AppLogs extends Model<AppLogs> implements Serializable {
    private static final long serialVersionUID = 3771563847025807470L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("username")
    private String username;

    @TableField("details")
    private String details;

    @TableField("url")
    private String url;

    @TableField("params")
    private String params;

    @TableField("create_time")
    private Date createTime;
}