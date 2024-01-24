package com.rms.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName property
 */
@TableName(value ="property")
@Data
public class Property implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 登录账户
     */
    @TableField(value = "account")
    private String account;

    /**
     * 登录密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 姓名
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
    private Integer sex;

    /**
     * 身份证
     */
    @TableField(value = "person_id")
    private String personId;

    /**
     * 账户"0启用" “1禁用”
     */
    @TableField(value = "state")
    private Integer state;

    /**
     * 是否在职
     */
    @TableField(value = "serving")
    private String serving;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}