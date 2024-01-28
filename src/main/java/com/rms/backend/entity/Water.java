package com.rms.backend.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName water
 */
@TableName(value ="water")
@Data
@ExcelTarget("water")
public class Water implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Excel(name = "序号",orderNum = "1")
    private Integer id;

    /**
     * 房屋id
     */
    @TableField(value = "hid")
    @Excel(name = "房屋id",orderNum = "2")
    private Integer hid;

    /**
     * 业主id
     */
    @TableField(value = "oid")
    @Excel(name = "业主id",orderNum = "3")
    private Integer oid;

    /**
     * 缴费年月
     */
    @TableField(value = "payment_time")
    @Excel(name = "缴费年月",orderNum = "4")
    private Date paymentTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @Excel(name = "更新时间",orderNum = "5")
    private Date updateTime;

    /**
     * 缴费金额
     */
    @TableField(value = "amount")
    @Excel(name = "缴费金额",orderNum = "6")
    private Double amount;

    /**
     * 余额
     */
    @TableField(value = "balance")
    @Excel(name = "余额",orderNum = "1")
    private Double balance;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}