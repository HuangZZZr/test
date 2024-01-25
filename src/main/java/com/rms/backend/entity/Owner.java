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
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 业主姓名
     */
    private String name;

    /**
     * 电话
     */
    private String tel;

    /**
     * 性别'0女 1男'
     */
    private String sex;

    /**
     * 登录用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 账户"0启用"“1禁用”
     */
    private Integer statue;

    private String salt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}