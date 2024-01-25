package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Complain;
import com.rms.backend.service.ComplainService;
import com.rms.backend.mapper.ComplainMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
* @author 16600
* @description 针对表【complain】的数据库操作Service实现
* @createDate 2024-01-23 08:45:59
*/
@Service
public class ComplainServiceImpl extends ServiceImpl<ComplainMapper, Complain> implements ComplainService{

    @Override
    public ResponseResult getComplainList(QueryCondition<Complain> queryCondition) {
        Page<Complain> complainPage = new Page<>(queryCondition.getLimit(), queryCondition.getPage());
        LambdaQueryWrapper<Complain> lambda = new QueryWrapper<Complain>().lambda();
        lambda.eq(ObjectUtils.isNotNull(queryCondition.getQuery().getStatue()),Complain::getStatue,queryCondition.getQuery().getStatue())
                .eq(ObjectUtils.isNotNull(queryCondition.getQuery().getOid()),Complain::getOid,queryCondition.getQuery().getOid())
                .orderByDesc(Complain::getComplainTime);

        baseMapper.selectPage(complainPage,lambda);

        HashMap<String, Object> result = new HashMap<>();
        result.put("pageData",complainPage.getRecords());
        result.put("total",complainPage.getTotal());
        return ResponseResult.success().data(result);

    }



}




