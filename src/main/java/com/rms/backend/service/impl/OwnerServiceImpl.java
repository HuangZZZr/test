package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.*;
import com.rms.backend.form.OwnerForm;
import com.rms.backend.mapper.*;
import com.rms.backend.service.OwnerDriService;
import com.rms.backend.service.OwnerService;
import com.rms.backend.utils.SaltUtil;
import com.rms.backend.vo.OwnerVO;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
* @author 16600
* @description 针对表【owner】的数据库操作Service实现
* @createDate 2024-01-15 13:48:37
*/
@Service
@Transactional
public class OwnerServiceImpl extends ServiceImpl<OwnerMapper, Owner> implements OwnerService{

    @Resource
    private OwnerRoleMapper ownerRoleMapper;

    @Resource
    private ElectricityMapper electricityMapper;

    @Resource
    private WaterMapper waterMapper;

    @Resource
    private ParkingFreeMapper parkingFreeMapper;
    @Resource
    private DrivewayMapper drivewayMapper;
    @Resource
    private PropertyMapper propertyMapper;
    @Resource
    private OwnerDriMapper ownerDriMapper;
    @Resource
    private OwnerDriService ownerDriService;
    @Resource
    private HouseMapper houseMapper;
    @Resource
    private  OwnerHouseMapper ownerHouseMapper;
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
    public ResponseResult saveOwner(OwnerVO ownerVO) {
        Owner owner = new Owner();
        BeanUtils.copyProperties(ownerVO,owner);

        String account = owner.getUsername();
        LambdaQueryWrapper<Owner> lambda = new QueryWrapper<Owner>().lambda();
        lambda.eq(Owner::getUsername,account);
        Owner owner1 = baseMapper.selectOne(lambda);

        LambdaQueryWrapper<Property> lambda1 = new QueryWrapper<Property>().lambda();
        lambda1.eq(Property::getAccount,account);
        Property property = propertyMapper.selectOne(lambda1);

        if(ObjectUtils.isNotNull(owner1)||ObjectUtils.isNotNull(property)){
            return ResponseResult.fail().message("已存在该账户");
        }
        String salt = SaltUtil.createSalt(4);
        Md5Hash md5Hash = new Md5Hash("666666", salt, 1024);
        String hex = md5Hash.toHex();
        owner.setSalt(salt);
        owner.setPassword(hex);
        owner.setStatue(0);
        baseMapper.insert(owner);

//        添加房屋关联
        Integer hid = houseMapper.selectOne(new QueryWrapper<House>().lambda().eq(House::getNumbering, ownerVO.getNumbering())).getId();
        OwnerHouse ownerHouse = new OwnerHouse();
        ownerHouse.setHid(hid);
        ownerHouse.setOid(owner.getId());
        ownerHouseMapper.insert(ownerHouse);

//        改变入住房屋状态
        House house = houseMapper.selectById(hid);
        house.setStatue(1);
        houseMapper.updateById(house);

        //设置水费表的余额和缴费金额为0
        Water water = new Water();
        water.setOid(owner.getId());
        water.setHid(hid);
        water.setPaymentTime(new Date());
        water.setUpdateTime(new Date());
        water.setAmount(0.00);
        water.setPayment(0.00);
        waterMapper.insert(water);

        //设置电费表的余额和缴费金额为0
        Electricity electricity = new Electricity();
        electricity.setOid(owner.getId());
        electricity.setHid(hid);
        electricity.setPaymentTime(new Date());
        electricity.setUpdateTime(new Date());
        electricity.setPayment(0.00);
        electricity.setAmount(0.00);
        electricityMapper.insert(electricity);

        //修改业主角色表
        OwnerRole ownerRole = new OwnerRole();
        ownerRole.setOid(owner.getId());
        ownerRole.setRid(3);
        ownerRoleMapper.insert(ownerRole);

        return ResponseResult.success().message("添加成功");
    }

    @Override
    public ResponseResult removeById(Integer id) {

        //删除业主角色关联表的信息
        LambdaQueryWrapper<OwnerRole> lambda = new QueryWrapper<OwnerRole>().lambda();
        lambda.eq(OwnerRole::getOid,id);
        ownerRoleMapper.delete(lambda);

//        修改房屋状态
        Integer hid = ownerHouseMapper.selectOne(new QueryWrapper<OwnerHouse>().lambda().eq(OwnerHouse::getOid, id)).getHid();
        House house = houseMapper.selectById(hid);
        house.setStatue(0);
        houseMapper.updateById(house);

        //删除水电费
        LambdaQueryWrapper<Electricity> lambda1 = new QueryWrapper<Electricity>().lambda();
        lambda1.eq(Electricity::getOid,id);
        electricityMapper.delete(lambda1);

        LambdaQueryWrapper<Water> lambda2 = new QueryWrapper<Water>().lambda();
        lambda2.eq(Water::getOid,id);
        waterMapper.delete(lambda2);

//      车位关联表判断（有删除，无结束）
        LambdaQueryWrapper<OwnerDri> lambda4 = new QueryWrapper<OwnerDri>().lambda();
        OwnerDri one = ownerDriService.getOne(lambda4);

        if(ObjectUtils.isNotEmpty(one)){
            lambda4.eq(OwnerDri::getOid,id);
            ownerDriMapper.delete(lambda4);

//      车位状态修改
            Driveway driveway = drivewayMapper.selectById(one.getDid());
            driveway.setStatue(0);
            drivewayMapper.updateById(driveway);

//            停车费表删除
            LambdaQueryWrapper<ParkingFree> lambda3 = new QueryWrapper<ParkingFree>().lambda();
            lambda3.eq(ParkingFree::getOid,id);
            parkingFreeMapper.delete(lambda3);
        }

//        业主房屋关联删除
        LambdaQueryWrapper<OwnerHouse> lambda5 = new QueryWrapper<OwnerHouse>().lambda();
        lambda5.in(OwnerHouse::getOid,id);
        ownerHouseMapper.delete(lambda5);

//        删除业主信息
        baseMapper.deleteById(id);
        return ResponseResult.success().message("删除成功");
    }


}




