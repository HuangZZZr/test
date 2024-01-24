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
 * @TableName parking_free
 */
@TableName(value ="parking_free")
@Data
public class ParkingFree implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 业主id
     */
    @TableField(value = "oid")
    private Integer oid;

    /**
     * 车位id
     */
    @TableField(value = "did")
    private Integer did;

    /**
     * 缴费年月
     */
    @TableField(value = "payment_time")
    private Date paymentTime;

    /**
     * 缴费金额
     */
    @TableField(value = "pay_money")
    private Integer payMoney;

    /**
     * 账户余额
     */
    @TableField(value = "balance")
    private Integer balance;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}