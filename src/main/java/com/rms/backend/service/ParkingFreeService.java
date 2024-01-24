package com.rms.backend.service;

import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.ParkingFree;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 16600
* @description 针对表【parking_free】的数据库操作Service
* @createDate 2024-01-08 15:13:24
*/
public interface ParkingFreeService extends IService<ParkingFree> {

    ResponseResult getParkingFree(QueryCondition<ParkingFree> queryCondition);
}
