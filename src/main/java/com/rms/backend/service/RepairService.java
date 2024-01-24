package com.rms.backend.service;

import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Repair;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 16600
* @description 针对表【repair】的数据库操作Service
* @createDate 2024-01-23 08:55:43
*/
public interface RepairService extends IService<Repair> {

    ResponseResult getRepairList(QueryCondition<Repair> queryCondition);
}
