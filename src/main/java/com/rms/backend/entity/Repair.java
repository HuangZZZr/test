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
 * @TableName repair
 */
@TableName(value ="repair")
@Data
public class Repair implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 业主id
     */
    @TableField(value = "oid")
    private Integer oid;

    /**
     * 联系电话
     */
    @TableField(value = "tel")
    private String tel;

    /**
     * 维修内容
     */
    @TableField(value = "context")
    private String context;

    /**
     * 报修时间
     */
    @TableField(value = "repair_time")
    private Date repairTime;

    /**
     * 维修状态
     */
    @TableField(value = "statue")
    private String statue;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}