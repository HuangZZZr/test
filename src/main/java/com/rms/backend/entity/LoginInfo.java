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
 * @TableName login_info
 */
@TableName(value ="login_info")
@Data
public class LoginInfo implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 用户ip
     */
    @TableField(value = "ip")
    private String ip;

    /**
     * 登录时间
     */
    @TableField(value = "time")
    private Date time;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}