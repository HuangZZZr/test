package com.rms.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName owner
 */
@TableName(value ="owner")
@Data
public class Owner implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 业主姓名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 电话
     */
    @TableField(value = "tel")
    private String tel;

    /**
     * 性别'0女 1男'
     */
    @TableField(value = "sex")
    private String sex;

    /**
     * 登录用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 账户"0启用"“1禁用”
     */
    @TableField(value = "statue")
    private Integer statue;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}