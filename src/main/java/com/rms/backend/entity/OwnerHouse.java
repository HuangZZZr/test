package com.rms.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName owner_house
 */
@TableName(value ="owner_house")
@Data
public class OwnerHouse implements Serializable {
    /**
     * 
     */
    @TableId(value = "oid")
    private Integer oid;

    /**
     * 
     */
    @TableField(value = "hid")
    private Integer hid;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}