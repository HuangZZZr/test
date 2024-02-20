package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.House;
import com.rms.backend.entity.Owner;
import com.rms.backend.entity.Water;
import com.rms.backend.from.WaterFrom;
import com.rms.backend.mapper.HouseMapper;
import com.rms.backend.mapper.OwnerMapper;
import com.rms.backend.service.WaterService;
import com.rms.backend.mapper.WaterMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author 刘恒
* @description 针对表【water】的数据库操作Service实现
* @createDate 2024-01-09 17:04:56
*/
@Service
public class WaterServiceImpl extends ServiceImpl<WaterMapper, Water>
    implements WaterService{

    @Resource
    private HouseMapper houseMapper;

    @Resource
    private OwnerMapper ownerMapper;

    @Override
    public ResponseResult getwaterData() {

        List<Water> datas=baseMapper.waterData();
        List<Map>waterdatas=datas.stream().map(data->{

            HashMap<String, Object> waterHashMap = new HashMap<>();
            Double balance = data.getAmount();
            String numbering = houseMapper.selectById(data.getHid()).getNumbering();
            waterHashMap.put("numbering",numbering);
            waterHashMap.put("balance",balance);
            return waterHashMap;
        }).collect(Collectors.toList());
        return ResponseResult.success().data(waterdatas);
    }

    @Override
    public ResponseResult waterList(QueryCondition<House> queryCondition) {

        String numbering = queryCondition.getQuery().getNumbering();
        Page<Water> waterPage = new Page<>(queryCondition.getPage(), queryCondition.getLimit());
        if (StringUtils.isEmpty(numbering)){
            baseMapper.selectPage(waterPage,null);
        }else {
            House house = houseMapper.selectOne(new QueryWrapper<House>().lambda().eq(House::getNumbering, numbering));
            Integer hid = house.getId();
            baseMapper.selectPage(waterPage,new QueryWrapper<Water>().lambda().eq(Water::getHid,hid));
        }
        List<Water> waters = waterPage.getRecords();
        List<Object> waterFroms=waters.stream().map(water -> {
            WaterFrom waterFrom = new WaterFrom();
            BeanUtils.copyProperties(water,waterFrom);
            Integer hid = water.getHid();
            Integer oid = water.getOid();
            Owner owner = ownerMapper.selectById(oid);
            House house = houseMapper.selectById(hid);
            waterFrom.setNumbering(house.getNumbering());
            waterFrom.setWname(owner.getName());
            return waterFrom;
        }).collect(Collectors.toList());

        long total = waterPage.getTotal();

        //封装结果
        HashMap<String, Object> waterMap = new HashMap<>();
        waterMap.put("pageData",waterFroms);
        waterMap.put("total",total);

        return ResponseResult.success().data(waterMap);
    }
}




