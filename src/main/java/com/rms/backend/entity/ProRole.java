package com.rms.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName pro_role
 */
@TableName(value ="pro_role")
@Data
public class ProRole implements Serializable {
    /**
     * 物业管理员id
     */
    @TableId(value = "pid")
    private Integer pid;

    /**
     * 对应角色id
     */
    @TableField(value = "rid")
    private Integer rid;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}