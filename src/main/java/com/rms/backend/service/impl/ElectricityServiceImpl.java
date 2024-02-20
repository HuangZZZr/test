package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Electricity;
import com.rms.backend.entity.House;
import com.rms.backend.entity.Owner;
import com.rms.backend.entity.Water;
import com.rms.backend.from.EleFrom;
import com.rms.backend.mapper.HouseMapper;
import com.rms.backend.mapper.OwnerMapper;
import com.rms.backend.service.ElectricityService;
import com.rms.backend.mapper.ElectricityMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author 刘恒
* @description 针对表【electricity】的数据库操作Service实现
* @createDate 2024-01-09 17:04:56
*/
@Service
public class ElectricityServiceImpl extends ServiceImpl<ElectricityMapper, Electricity>
    implements ElectricityService{


    @Resource
    private HouseMapper houseMapper;

    @Resource
    private OwnerMapper ownerMapper;

    @Override
    public ResponseResult getelectricityData() {

        List<Electricity>datas=baseMapper.electricityData();
        List<Map> eledatas=datas.stream().map(data ->{
            Double balance = data.getAmount();
            HashMap<String,Object> eleHashMap = new HashMap<>();
            String numbering = houseMapper.selectById(data.getHid()).getNumbering();
            eleHashMap.put("numbering",numbering);
            eleHashMap.put("ebefore",data.getEbefore());
            eleHashMap.put("enow",data.getEnow());
            eleHashMap.put("balance",balance);
            return eleHashMap;
        }).collect(Collectors.toList());
        return ResponseResult.success().data(eledatas);
    }

    @Override
    public ResponseResult eleList(QueryCondition<House> queryCondition) {


        String numbering = queryCondition.getQuery().getNumbering();
        Page<Electricity> elePage = new Page<>(queryCondition.getPage(), queryCondition.getLimit());
        if (StringUtils.isEmpty(numbering)){
            baseMapper.selectPage(elePage,null);
        }else {
        Integer hid = houseMapper.selectOne(new QueryWrapper<House>().lambda().eq(House::getNumbering, numbering)).getId();
        baseMapper.selectPage(elePage,new QueryWrapper<Electricity>().lambda().eq(Electricity::getHid,hid));
        }
        List<Electricity> eles = elePage.getRecords();
        List<Object> eleFroms=eles.stream().map(electricity -> {
            EleFrom eleFrom = new EleFrom();
            BeanUtils.copyProperties(electricity,eleFrom);
            Integer hid = electricity.getHid();
            Integer oid = electricity.getOid();
            Owner owner = ownerMapper.selectById(oid);
            House house = houseMapper.selectById(hid);
            eleFrom.setEname(owner.getName());
            eleFrom.setNumbering(house.getNumbering());
            return eleFrom;
        }).collect(Collectors.toList());
        long total = elePage.getTotal();

        //封装结果
        HashMap<String, Object> eleMap = new HashMap<>();
        eleMap.put("pageData",eleFroms);
        eleMap.put("total",total);

        return ResponseResult.success().data(eleMap);

    }
}




