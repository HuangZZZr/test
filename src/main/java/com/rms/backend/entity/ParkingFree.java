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
 * @TableName parking_free
 */
@TableName(value ="parking_free")
@Data
@ExcelTarget("parkingFee")
public class ParkingFree implements Serializable {
    /**
     * 主键
     */
    @Excel(name = "序号",orderNum = "1")
    @TableId(value = "id")
    private Integer id;

    /**
     * 业主id
     */
    @Excel(name = "业主id",orderNum = "2")
    @TableField(value = "oid")
    private Integer oid;

    /**
     * 车位id
     */
    @Excel(name = "车位号",orderNum = "3")
    @TableField(value = "did")
    private Integer did;

    /**
     * 缴费年月
     */
    @Excel(name = "缴费时间",orderNum = "4",exportFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "payment_time")
    private Date paymentTime;

    /**
     * 缴费金额
     */
    @Excel(name = "缴费金额",orderNum = "5")
    @TableField(value = "pay_money")
    private Integer payMoney;

    /**
     * 账户余额
     */
    @Excel(name = "余额",orderNum = "6")
    @TableField(value = "balance")
    private Integer balance;

    /**
     * 更新时间
     */
    @Excel(name = "更新时间",orderNum = "7",exportFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}