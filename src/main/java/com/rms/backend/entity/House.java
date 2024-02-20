package com.rms.backend.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
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
@ExcelTarget("house")
public class House implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
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
    @Excel(name = "房屋编号")
    private String numbering;

    /**
     * 房屋面积
     */
    @TableField(value = "area")
    @Excel(name = "房屋面积")
    private Integer area;

    /**
     * 使用状态（0空闲，1已出售）
     */
    @TableField(value = "statue")
    @Excel(name = "出售状态")
    private Integer statue;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}