package com.rms.backend.service;

import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Driveway;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author YiXin
* @description 针对表【driveway】的数据库操作Service
* @createDate 2024-01-23 13:49:29
*/
public interface DrivewayService extends IService<Driveway> {

    ResponseResult selList(QueryCondition<Driveway> queryCondition);

    ResponseResult delBatchDriveway(Integer[] ids);

    ResponseResult saveOrUpdateDavewat(Driveway driveway);
}
