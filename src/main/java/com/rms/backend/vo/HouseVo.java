package com.rms.backend.vo;

import com.rms.backend.entity.House;
import lombok.Data;

/**
 * @Projectname: rms-backend
 * @Filename: HouseVo
 * @Author: LH
 * @Data:2024/1/11 16:28
 */
@Data
public class HouseVo extends House {

    //房主姓名
    private String houseName;
}
