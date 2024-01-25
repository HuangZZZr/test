package com.rms.backend.vo;

import com.rms.backend.entity.House;
import lombok.Data;

/**
 * @Projectname: rms-backend
 * @Filename: HouseViewVo
 * @Author: LH
 * @Data:2024/1/11 17:18
 */
@Data
public class HouseViewVo extends House {

    public String houseName;

    public Double water;

    public Double ele;
}
