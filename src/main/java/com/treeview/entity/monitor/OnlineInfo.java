package com.treeview.entity.monitor;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("online_info")
public class OnlineInfo implements Serializable {
    private static final long serialVersionUID = -2718862656694464429L;

    @TableId
    private Long id;

    @TableField("user_name")
    private String userName;

    @TableField("session")
    private String session;

    @TableField("ip_addr")
    private String ipAddr;

    @TableField("browser")
    private String browser;

    @TableField("os_name")
    private String osName;

    @TableField("last_login_time")
    private Date lastLoginTime;

    @TableField("last_visit_time")
    private Date lastVisitTime;

    @TableField(exist = false)
    /** 0-离线 1-活跃 **/
    private Integer status;
}