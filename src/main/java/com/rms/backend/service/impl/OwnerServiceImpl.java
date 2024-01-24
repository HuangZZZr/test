package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Electricity;
import com.rms.backend.entity.Owner;
import com.rms.backend.entity.OwnerRole;
import com.rms.backend.entity.Water;
import com.rms.backend.form.OwnerForm;
import com.rms.backend.mapper.ElectricityMapper;
import com.rms.backend.mapper.OwnerRoleMapper;
import com.rms.backend.mapper.WaterMapper;
import com.rms.backend.service.OwnerService;
import com.rms.backend.mapper.OwnerMapper;
import com.rms.backend.utils.SaltUtil;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
* @author 16600
* @description 针对表【owner】的数据库操作Service实现
* @createDate 2024-01-15 13:48:37
*/
@Service
public class OwnerServiceImpl extends ServiceImpl<OwnerMapper, Owner> implements OwnerService{

    @Resource
    private OwnerRoleMapper ownerRoleMapper;

    @Resource
    private ElectricityMapper electricityMapper;

    @Resource
    private WaterMapper waterMapper;
    @Override
    public ResponseResult ownerList(QueryCondition<Owner> queryCondition) {
        //分页查询
        Page<Owner> ownerPage = new Page<>(queryCondition.getPage(), queryCondition.getLimit());

        //查询条件
        LambdaQueryWrapper<Owner> lambda = new QueryWrapper<Owner>().lambda();
        lambda.like(StringUtils.isNotEmpty(queryCondition.getQuery().getUsername()),Owner::getUsername,queryCondition.getQuery().getUsername())
                .eq(ObjectUtils.isNotNull(queryCondition.getQuery().getStatue()),Owner::getStatue,queryCondition.getQuery().getStatue());

        baseMapper.selectPage(ownerPage,lambda);
        List<Owner> owners = ownerPage.getRecords();


        // 封装响应结果
        HashMap<String, Object> result = new HashMap<>();
        result.put("pageData", owners);
        result.put("total", ownerPage.getTotal());
        return ResponseResult.success().data(result);
    }

    @Override
    public ResponseResult saveOwner(OwnerForm ownerForm) {
        Owner owner = new Owner();
        BeanUtils.copyProperties(ownerForm,owner);

        String name = owner.getName();
        LambdaQueryWrapper<Owner> lambda = new QueryWrapper<Owner>().lambda();
        lambda.eq(Owner::getName,name);
        Owner owner1 = baseMapper.selectOne(lambda);
        if(ObjectUtils.isNotNull(owner1)){
            return ResponseResult.fail().message("已存在该业主");
        }
        String salt = SaltUtil.createSalt(4);
        Md5Hash md5Hash = new Md5Hash("666666", salt, 1024);
        String hex = md5Hash.toHex();
        owner.setSalt(salt);
        owner.setPassword(hex);

        //设置水费表的余额和缴费金额为0
        Water water = new Water();
        water.setOid(owner.getId());
        water.setAmount(0.00);
        water.setBalance(0.00);

        //设置电费表的余额和缴费金额为0
        Electricity electricity = new Electricity();
        electricity.setOid(owner.getId());
        electricity.setBalance(0.00);
        electricity.setAmount(0.00);

        baseMapper.insert(owner);



        //修改业主角色表
        Integer id = owner.getId();
        List<Integer> rids = ownerForm.getRids();
        rids.forEach(rid ->{
            OwnerRole ownerRole = new OwnerRole();
            ownerRole.setOid(id);
            ownerRole.setRid(rid);
            ownerRoleMapper.insert(ownerRole);
        });
        return ResponseResult.success().message("添加成功");
    }

    @Override
    public ResponseResult editOwner(OwnerForm ownerForm) {
        //先修改owner表的数据
        Owner owner = new Owner();
        BeanUtils.copyProperties(ownerForm,owner);
        baseMapper.updateById(owner);

        //删除当前用户的角色，再将新的角色信息插入角色表中
        LambdaQueryWrapper<OwnerRole> lambda = new QueryWrapper<OwnerRole>().lambda();
        lambda.eq(OwnerRole::getOid,owner.getId());
        ownerRoleMapper.delete(lambda);

        List<Integer> rids = ownerForm.getRids();
        rids.forEach(rid->{
            OwnerRole ownerRole = new OwnerRole();
           ownerRole.setRid(rid);
           ownerRole.setOid(owner.getId());
           ownerRoleMapper.insert(ownerRole);
        });
        return ResponseResult.success().message("修改成功");
    }

    @Override
    public ResponseResult batchRemoveByIds(List<Integer> ids) {

        //删除业主表的信息
        baseMapper.deleteBatchIds(ids);

        //删除业主角色关联表的信息
        LambdaQueryWrapper<OwnerRole> lambda = new QueryWrapper<OwnerRole>().lambda();
        lambda.in(OwnerRole::getOid,ids);
        ownerRoleMapper.delete(lambda);

        //删除水电费和车位费

        return ResponseResult.success().message("删除成功");
    }


}




