package com.rms.backend.service;

import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Electricity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rms.backend.entity.House;

/**
* @author 刘恒
* @description 针对表【electricity】的数据库操作Service
* @createDate 2024-01-09 17:04:56
*/
public interface ElectricityService extends IService<Electricity> {

    ResponseResult getelectricityData();

    ResponseResult eleList(QueryCondition<House> queryCondition);
}
