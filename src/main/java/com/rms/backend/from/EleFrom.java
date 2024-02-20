package com.rms.backend.from;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.rms.backend.entity.Electricity;
import lombok.Data;

/**
 * @Projectname: rms-backend
 * @Filename: EleFrom
 * @Author: LH
 * @Data:2024/1/22 14:00
 */

@Data
public class EleFrom extends Electricity {
    @Excel(name = "房屋号",orderNum = "2")
    private String numbering;
    @Excel(name = "业主姓名",orderNum = "3")
    private String ename;
}
