package com.rms.backend.service;

import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Driveway;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 16600
* @description 针对表【driveway】的数据库操作Service
* @createDate 2024-01-06 11:16:54
*/
public interface DrivewayService extends IService<Driveway> {

    ResponseResult getDriveWayList(QueryCondition<Driveway> queryCondition);
}
