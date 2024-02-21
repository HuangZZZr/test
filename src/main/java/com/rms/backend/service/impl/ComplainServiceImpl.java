package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Complain;
import com.rms.backend.entity.Owner;
import com.rms.backend.form.ComplainForm;
import com.rms.backend.mapper.OwnerMapper;
import com.rms.backend.service.ComplainService;
import com.rms.backend.mapper.ComplainMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author 16600
* @description 针对表【complain】的数据库操作Service实现
* @createDate 2024-01-23 08:45:59
*/
@Service
public class ComplainServiceImpl extends ServiceImpl<ComplainMapper, Complain> implements ComplainService{
    @Resource
    private OwnerMapper ownerMapper;
    @Override
    public ResponseResult getComplainList(QueryCondition<Complain> queryCondition) {
        Page<Complain> complainPage = new Page<>( queryCondition.getPage(),queryCondition.getLimit());
        LambdaQueryWrapper<Complain> lambda = new QueryWrapper<Complain>().lambda();
        lambda.eq(ObjectUtils.isNotNull(queryCondition.getQuery().getStatue()),Complain::getStatue,queryCondition.getQuery().getStatue())
                .eq(ObjectUtils.isNotNull(queryCondition.getQuery().getOid()),Complain::getOid,queryCondition.getQuery().getOid())
                .orderByDesc(Complain::getComplainTime);

        baseMapper.selectPage(complainPage,lambda);

        List<Complain> records = complainPage.getRecords();
        List<ComplainForm> collect = records.stream().map(complain -> {
            ComplainForm complainForm = new ComplainForm();
            Integer oid = complain.getOid();
            BeanUtils.copyProperties(complain, complainForm);
            Owner owner = ownerMapper.selectById(oid);
            if (ObjectUtils.isEmpty(owner)){
                complainForm.setAccount("用户已搬离");
            }else {
                complainForm.setAccount(owner.getName());
            }
            return complainForm;
        }).collect(Collectors.toList());

        HashMap<String, Object> result = new HashMap<>();
        result.put("pageData",collect);
        result.put("total",complainPage.getTotal());
        return ResponseResult.success().data(result);

    }



}




