package com.rms.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName owner_dri
 */
@TableName(value ="owner_dri")
@Data
public class OwnerDri implements Serializable {
    /**
     * 业主id
     */
    @TableId(value = "oid")
    private Integer oid;

    /**
     * 车位id
     */
    @TableField(value = "did")
    private Integer did;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}