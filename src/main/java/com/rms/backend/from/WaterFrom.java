package com.rms.backend.from;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.rms.backend.entity.Water;
import lombok.Data;

/**
 * @Projectname: rms-backend
 * @Filename: WaterFrom
 * @Author: LH
 * @Data:2024/1/22 13:41
 */
@Data
@ExcelTarget("waterFrom")
public class WaterFrom extends Water {

    @Excel(name = "房屋号",orderNum = "2")
    private String numbering;

    @Excel(name = "业主姓名",orderNum = "3")
    private String wname;
}
