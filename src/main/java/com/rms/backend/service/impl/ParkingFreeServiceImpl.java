package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.ParkingFree;
import com.rms.backend.service.ParkingFreeService;
import com.rms.backend.mapper.ParkingFreeMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
* @author 16600
* @description 针对表【parking_free】的数据库操作Service实现
* @createDate 2024-01-08 15:13:24
*/
@Service
public class ParkingFreeServiceImpl extends ServiceImpl<ParkingFreeMapper, ParkingFree> implements ParkingFreeService{

    @Override
    public ResponseResult getParkingFree(QueryCondition<ParkingFree> queryCondition) {
        Page<ParkingFree> parkingFreePage = new Page<>(queryCondition.getPage(), queryCondition.getLimit());
        LambdaQueryWrapper<ParkingFree> lambda = new QueryWrapper<ParkingFree>().lambda();
        lambda.like(ObjectUtils.isNotNull(queryCondition.getQuery().getPaymentTime()),ParkingFree::getPaymentTime,queryCondition.getQuery().getPaymentTime());
        baseMapper.selectPage(parkingFreePage,lambda);

        HashMap<String, Object> result = new HashMap<>();
        result.put("pageData",parkingFreePage.getRecords());
        result.put("total",parkingFreePage.getTotal());

        return ResponseResult.success().data(result);
    }


}






