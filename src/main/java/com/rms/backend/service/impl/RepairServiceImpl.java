package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Repair;
import com.rms.backend.service.RepairService;
import com.rms.backend.mapper.RepairMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
* @author 16600
* @description 针对表【repair】的数据库操作Service实现
* @createDate 2024-01-23 08:55:43
*/
@Service
public class RepairServiceImpl extends ServiceImpl<RepairMapper, Repair> implements RepairService{

    @Override
    public ResponseResult getRepairList(QueryCondition<Repair> queryCondition) {

        Page<Repair> repairPage = new Page<>(queryCondition.getLimit(), queryCondition.getPage());
        LambdaQueryWrapper<Repair> lambda = new QueryWrapper<Repair>().lambda();
        lambda.eq(ObjectUtils.isNotNull(queryCondition.getQuery().getOid()), Repair::getOid, queryCondition.getQuery().getOid())
                .eq(ObjectUtils.isNotNull(queryCondition.getQuery().getStatue()), Repair::getStatue, queryCondition.getQuery().getStatue())
                .orderByDesc(Repair::getRepairTime);

        baseMapper.selectPage(repairPage, lambda);

        HashMap<String, Object> result = new HashMap<>();
        result.put("pageData", repairPage.getRecords());
        result.put("total", repairPage.getTotal());
        return ResponseResult.success().data(result);
    }

    @Override
    public ResponseResult getpepairData() {

        List<Repair> datas=baseMapper.pepairData();
        return ResponseResult.success().data(datas);
    }
}




