package com.rms.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName house
 */
@TableName(value ="house")
@Data
public class House implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 单元id
     */
    @TableField(value = "uid")
    private Integer uid;

    /**
     * 房屋编号
     */
    @TableField(value = "numbering")
    private String numbering;

    /**
     * 房屋面积
     */
    @TableField(value = "area")
    private Integer area;

    /**
     * 使用状态（0空闲，1已出售）
     */
    @TableField(value = "statue")
    private Integer statue;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}