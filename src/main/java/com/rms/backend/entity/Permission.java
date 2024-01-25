package com.rms.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName permission
 */
@TableName(value ="permission")
@Data
public class Permission implements Serializable {
    /**
     * 权限id
     */
    @TableId
    private Integer id;

    /**
     * 菜单
     */
    private String title;

    /**
     * 父id
     */
    private Integer pid;

    /**
     * 权限字符串
     */
    private String permission;

    /**
     * 模块
     */
    private String model;

    /**
     * 组件路径
     */
    private String path;

    /**
     * 组件名称
     */
    private String component;

    /**
     * 图标
     */
    private String icon;

    /**
     * 是否是菜单  0 目录  1菜单   2 按钮
     */
    private Integer isMenu;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}