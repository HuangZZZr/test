package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Electricity;
import com.rms.backend.entity.House;
import com.rms.backend.entity.Water;
import com.rms.backend.mapper.HouseMapper;
import com.rms.backend.service.ElectricityService;
import com.rms.backend.mapper.ElectricityMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

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

    @Override
    public ResponseResult getelectricityData() {

        List<Electricity>datas=baseMapper.electricityData();
        return ResponseResult.success().data(datas);
    }

    @Override
    public ResponseResult eleList(QueryCondition<House> queryCondition) {


        String numbering = queryCondition.getQuery().getNumbering();
        Page<Electricity> elePage = new Page<>(queryCondition.getPage(), queryCondition.getLimit());
        if (StringUtils.isEmpty(numbering)){
            baseMapper.selectPage(elePage,null);
        }
        Integer hid = houseMapper.selectOne(new QueryWrapper<House>().lambda().eq(House::getNumbering, numbering)).getId();
        baseMapper.selectPage(elePage,new QueryWrapper<Electricity>().lambda().eq(Electricity::getHid,hid));

        List<Electricity> eles = elePage.getRecords();
        long total = elePage.getTotal();

        //封装结果
        HashMap<String, Object> eleMap = new HashMap<>();
        eleMap.put("pageData",eles);
        eleMap.put("total",total);

        return ResponseResult.success().data(eleMap);

    }
}




