package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Unit;
import com.rms.backend.service.UnitService;
import com.rms.backend.mapper.UnitMapper;
import org.springframework.stereotype.Service;

/**
* @author 16600
* @description 针对表【unit】的数据库操作Service实现
* @createDate 2024-01-24 13:39:40
*/
@Service
public class UnitServiceImpl extends ServiceImpl<UnitMapper, Unit> implements UnitService{

    @Override
    public ResponseResult getUnitList(QueryCondition<Unit> queryCondition) {
        Page<Unit> unitPage = new Page<>(queryCondition.getLimit(), queryCondition.getPage());
        LambdaQueryWrapper<Unit> lambda = new QueryWrapper<Unit>().lambda();
        lambda.eq(StringUtils.isNotEmpty(queryCondition.getQuery().getName()),Unit::getName,queryCondition.getQuery().getName())

        return null;
    }
}




