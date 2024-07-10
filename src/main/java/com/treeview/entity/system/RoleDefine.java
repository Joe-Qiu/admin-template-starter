package com.treeview.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("role_define")
public class RoleDefine extends Model<RoleDefine> implements Serializable{
    private static final long serialVersionUID = -5323682584726717177L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("role_name")
    private String roleName;

    @TableField("role_desc")
    private String roleDesc;

    @TableField("state")
    private Integer state;

    @TableField("create_time")
    private Date createTime;
}