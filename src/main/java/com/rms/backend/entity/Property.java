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
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 登录账户
     */
    private String account;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 电话
     */
    private String tel;

    /**
     * 性别'0女 1男'
     */
    private Integer sex;

    /**
     * 身份证
     */
    private String personId;

    /**
     * 账户"0启用" “1禁用”
     */
    private Integer state;

    /**
     * 是否在职
     */
    private String serving;

    private String salt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}