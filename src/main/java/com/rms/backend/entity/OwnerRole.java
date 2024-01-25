package com.rms.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName owner_role
 */
@TableName(value ="owner_role")
@Data
public class OwnerRole implements Serializable {
    /**
     * 业主id
     */
    @TableId
    private Integer oid;

    /**
     * 角色id
     */
    private Integer rid;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}