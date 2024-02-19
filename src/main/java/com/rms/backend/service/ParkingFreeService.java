package com.rms.backend.service;

import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Driveway;
import com.rms.backend.entity.ParkingFree;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author YiXin
* @description 针对表【parking_free】的数据库操作Service
* @createDate 2024-01-24 10:36:04
*/
public interface ParkingFreeService extends IService<ParkingFree> {

    ResponseResult parkingFeeList(QueryCondition<Driveway> queryCondition);

    ResponseResult delParkingFees(Integer[] pIds);
}
