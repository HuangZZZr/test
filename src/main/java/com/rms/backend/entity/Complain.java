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
 * @TableName complain
 */
@TableName(value ="complain")
@Data
public class Complain implements Serializable {
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
     * 投诉内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 投诉时间
     */
    @TableField(value = "complain_time")
    private Date complainTime;

    /**
     * 处理状态（0否 ， 1是）
     */
    @TableField(value = "statue")
    private Integer statue;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}