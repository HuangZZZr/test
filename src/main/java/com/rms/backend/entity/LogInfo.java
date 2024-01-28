package com.rms.backend.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName log_info
 */
@TableName(value ="log_info")
@Data
@ExcelTarget("log")
public class LogInfo implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 操作人
     */
    @TableField(value = "name")
    @Excel(name = "操作人")
    private String name;

    /**
     * 用户ip
     */
    @TableField(value = "ip")
    @Excel(name = "用户ip")
    private String ip;

    /**
     * 参数
     */
    @TableField(value = "params")
    @Excel(name = "参数")
    private String params;

    /**
     * 资源路径
     */
    @TableField(value = "url")
    @Excel(name = "资源路径")
    private String url;

    /**
     * 操作描述
     */
    @TableField(value = "description")
    @Excel(name = "操作描述")
    private String description;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @Excel(name = "创建时间")
    private Date createTime;

    /**
     * 模块
     */
    @TableField(value = "model")
    @Excel(name = "模块")
    private String model;

    /**
     * 耗时
     */
    @TableField(value = "time_consuming")
    @Excel(name = "耗时")
    private Integer timeConsuming;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}