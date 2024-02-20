package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Owner;
import com.rms.backend.entity.Repair;
import com.rms.backend.form.RepairForm;
import com.rms.backend.mapper.OwnerMapper;
import com.rms.backend.service.RepairService;
import com.rms.backend.mapper.RepairMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author 16600
* @description 针对表【repair】的数据库操作Service实现
* @createDate 2024-01-23 08:55:43
*/
@Service
public class RepairServiceImpl extends ServiceImpl<RepairMapper, Repair> implements RepairService{
    @Resource
    private OwnerMapper ownerMapper;
    @Override
    public ResponseResult getRepairList(QueryCondition<Owner> queryCondition) {

        Page<Repair> repairPage = new Page<>( queryCondition.getPage(),queryCondition.getLimit());
//        获取用户id（提前判断用户是否存在）
        Integer oid = null;
        if (ObjectUtils.isNotEmpty(queryCondition.getQuery().getUsername())){
            Owner one = ownerMapper.selectOne(new QueryWrapper<Owner>().lambda().eq(Owner::getUsername, queryCondition.getQuery().getUsername()));
            if (ObjectUtils.isNotEmpty(one)){
                oid = one.getId();
            }else {
                return ResponseResult.fail().message("无该业主维修记录");
            }
        }

        LambdaQueryWrapper<Repair> lambda = new QueryWrapper<Repair>().lambda();
        lambda.eq(ObjectUtils.isNotNull(oid), Repair::getOid, oid)
                .eq(ObjectUtils.isNotNull(queryCondition.getQuery().getStatue()), Repair::getStatue, queryCondition.getQuery().getStatue())
                .orderByDesc(Repair::getRepairTime);

        baseMapper.selectPage(repairPage, lambda);


        List<Repair> records = repairPage.getRecords();
        List<RepairForm> repairForms = records.stream().map(repair -> {
            RepairForm repairForm = new RepairForm();
            Integer newOid = repair.getOid();
            BeanUtils.copyProperties(repair, repairForm);
            Owner owner = ownerMapper.selectById(newOid);
            repairForm.setAccount(owner.getUsername());

            return repairForm;
        }).collect(Collectors.toList());

        HashMap<String, Object> result = new HashMap<>();
        result.put("pageDate",repairForms);
        result.put("total",repairPage.getTotal());
        return ResponseResult.success().data(result);

    }

    @Override
    public ResponseResult getpepairData() {

        List<Repair> datas=baseMapper.pepairData();
        List<Object> repairFroms=datas.stream().map(data -> {
            RepairForm repairForm = new RepairForm();
            BeanUtils.copyProperties(data,repairForm);
            Integer oid = data.getOid();
            Owner owner = ownerMapper.selectById(oid);
            repairForm.setAccount( owner.getUsername());
            return repairForm;
        }).collect(Collectors.toList());
        return ResponseResult.success().data(repairFroms);
    }
}




