package com.treeview.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("user_info")
public class UserInfo implements Serializable {
    private static final long serialVersionUID = -2941077060508091741L;
    
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("dept_id")
    private Long deptId;

    @TableField("user_name")
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "用户名不能带有除英文字母和数字以外的符号")
    @Length(min = 6, max = 12, message = "用户名长度必须在6至12位")
    private String userName;

    @TableField("nick_name")
    private String nickName;

    @TableField("user_type")
    private String userType;

    @TableField("email")
    @Email(message = "无效的邮件地址")
    private String email;

    @Length(min = 11, max = 11, message = "手机号只能为11位")
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
    @TableField("phone")
    private String phone;

    @TableField("gender")
    private String gender;

    @TableField("avatar")
    private String avatar;

    @TableField("password")
    private String passWord;

    @TableField("salt")
    private String salt;

    @TableField("status")
    private String status;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;
}