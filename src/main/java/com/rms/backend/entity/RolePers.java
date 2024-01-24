package com.rms.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName role_pers
 */
@TableName(value ="role_pers")
@Data
public class RolePers implements Serializable {
    /**
     * 
     */
    @TableId(value = "rid")
    private Integer rid;

    /**
     * 
     */
    @TableField(value = "pid")
    private Integer pid;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}