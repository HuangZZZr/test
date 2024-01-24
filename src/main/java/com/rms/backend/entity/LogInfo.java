package com.rms.backend.entity;

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
    private String name;

    /**
     * 用户ip
     */
    @TableField(value = "ip")
    private String ip;

    /**
     * 参数
     */
    @TableField(value = "params")
    private String params;

    /**
     * 资源路径
     */
    @TableField(value = "url")
    private String url;

    /**
     * 操作描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 模块
     */
    @TableField(value = "model")
    private String model;

    /**
     * 耗时
     */
    @TableField(value = "time_consuming")
    private Integer timeConsuming;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}