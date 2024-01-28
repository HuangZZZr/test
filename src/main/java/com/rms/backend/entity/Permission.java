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
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 组件名称
     */
    @TableField(value = "title")
    private String title;

    /**
     * 父id
     */
    @TableField(value = "pid")
    private Integer pid;

    /**
     * 权限字符串
     */
    @TableField(value = "permission")
    private String permission;

    /**
     * 模块
     */
    @TableField(value = "model")
    private String model;

    /**
     * 组件路径
     */
    @TableField(value = "path")
    private String path;

    /**
     * 组件名称
     */
    @TableField(value = "component")
    private String component;

    /**
     * 图标
     */
    @TableField(value = "icon")
    private String icon;

    /**
     * 是否是菜单  0 目录  1菜单   2 按钮
     */
    @TableField(value = "is_menu")
    private Integer isMenu;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}