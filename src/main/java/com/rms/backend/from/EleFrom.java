package com.rms.backend.from;

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
    private String numbering;
    private String ename;
}
