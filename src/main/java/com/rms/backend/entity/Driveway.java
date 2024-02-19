package com.rms.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName driveway
 */
@TableName(value ="driveway")
@Data
public class Driveway implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 车位号
     */
    @TableField(value = "nos")
    private String nos;

    /**
     * 车位类型（A，B，C），体现大小
     */
    @TableField(value = "car_kind")
    private String carKind;

    /**
     * 状态（0未用 ， 1已被使用）
     */
    @TableField(value = "statue")
    private Integer statue;

    /**
     * 栋数
     */
    @TableField(value = "ridgepole")
    private String ridgepole;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}