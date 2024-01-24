package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Driveway;
import com.rms.backend.service.DrivewayService;
import com.rms.backend.mapper.DrivewayMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
* @author 16600
* @description 针对表【driveway】的数据库操作Service实现
* @createDate 2024-01-06 11:16:54
*/
@Service
public class DrivewayServiceImpl extends ServiceImpl<DrivewayMapper, Driveway> implements DrivewayService{

    @Override
    public ResponseResult getDriveWayList(QueryCondition<Driveway> queryCondition) {
        Page<Driveway> drivewayPage = new Page<>(queryCondition.getPage(), queryCondition.getLimit());
        LambdaQueryWrapper<Driveway> lambda = new QueryWrapper<Driveway>().lambda();
        lambda.like(StringUtils.isNotEmpty(queryCondition.getQuery().getCarKind()),Driveway::getCarKind,queryCondition.getQuery().getCarKind())
                .eq(ObjectUtils.isNotNull(queryCondition.getQuery().getStatue()),Driveway::getStatue,queryCondition.getQuery().getStatue());

        baseMapper.selectPage(drivewayPage,lambda);

        HashMap<String, Object> result = new HashMap<>();
        result.put("pageData",drivewayPage.getRecords());
        result.put("total",drivewayPage.getTotal());

        return ResponseResult.success().data(result);
    }
}




