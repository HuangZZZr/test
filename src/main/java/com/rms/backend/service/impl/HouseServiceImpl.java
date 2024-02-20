package com.rms.backend.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Electricity;
import com.rms.backend.entity.House;
import com.rms.backend.entity.OwnerHouse;
import com.rms.backend.entity.Water;
import com.rms.backend.exceptions.HouseNoException;
import com.rms.backend.mapper.*;
import com.rms.backend.service.HouseService;
import com.rms.backend.service.OwnerHouseService;
import com.rms.backend.vo.HouseViewVo;
import com.rms.backend.vo.HouseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author 刘恒
* @description 针对表【house】的数据库操作Service实现
* @createDate 2024-01-10 17:13:54
*/
@Service
@Transactional
@Slf4j
public class HouseServiceImpl extends ServiceImpl<HouseMapper, House>
    implements HouseService{

    @Resource
    private HouseMapper houseMapper;

    @Resource
     private OwnerHouseMapper ownerHouseMapper;

    @Resource
    private OwnerMapper ownerMapper;


    @Resource
     private WaterMapper waterMapper;

    @Resource
    private ElectricityMapper electricityMapper;

    @Override
    public ResponseResult gethouseData() {
        List<Map<Integer,Integer>>datas=baseMapper.houseData();
        return ResponseResult.success().data(datas);
    }

    @Override
    public ResponseResult houseList(QueryCondition<House> queryCondition) {

        Page<House> housePage = new Page<>(queryCondition.getPage(), queryCondition.getLimit());
        LambdaQueryWrapper<House> lambda = new QueryWrapper<House>().lambda();
        lambda.like(ObjectUtils.isNotEmpty(queryCondition.getQuery().getUid()),House::getUid,queryCondition.getQuery().getUid())
                .eq(StringUtils.isNotEmpty(queryCondition.getQuery().getNumbering()),House::getNumbering,queryCondition.getQuery().getNumbering())
                .eq(ObjectUtils.isNotEmpty(queryCondition.getQuery().getStatue()),House::getStatue,queryCondition.getQuery().getStatue());

        baseMapper.selectPage(housePage,lambda);

        List<House> houses = housePage.getRecords();
        List<Object> houseVOS= houses.stream().map(house -> {
            HouseVo houseVo = new HouseVo();
            BeanUtils.copyProperties(house,houseVo);


            //根据houseID查询屋主姓名
            Integer houseId = house.getId();
                LambdaQueryWrapper<OwnerHouse> queryWrapper = new QueryWrapper<OwnerHouse>().lambda();
                queryWrapper.eq(OwnerHouse::getHid,houseId);
                OwnerHouse ownerHouse = ownerHouseMapper.selectOne(queryWrapper);
            if (ObjectUtils.isNotEmpty(ownerHouse)){
                Integer oid = ownerHouse.getOid();
                String name = ownerMapper.selectById(oid).getName();
                houseVo.setHouseName(name);
            }else {
                houseVo.setHouseName(null);
            }

            return houseVo;
        }).collect(Collectors.toList());

        //封装结果
        HashMap<String, Object> houseMap = new HashMap<>();
        houseMap.put("pageData",houseVOS);
        houseMap.put("total",housePage.getTotal());

        return ResponseResult.success().data(houseMap);
    }

    @Override
    public ResponseResult veiwHouse(Integer id) {
        HouseViewVo houseViewVo = new HouseViewVo();
        House house = houseMapper.selectById(id);
        BeanUtils.copyProperties(house,houseViewVo);


        //根据houseID查询屋主姓名
        LambdaQueryWrapper<OwnerHouse> queryWrapper = new QueryWrapper<OwnerHouse>().lambda();
        queryWrapper.eq(OwnerHouse::getHid,id);
        OwnerHouse ownerHouse = ownerHouseMapper.selectOne(queryWrapper);
        if (ObjectUtils.isNotEmpty(ownerHouse)){
            Integer oid = ownerHouse.getOid();
            String name = ownerMapper.selectById(oid).getName();
            houseViewVo.setHouseName(name);
        }

        //根据hid查询水费
        LambdaQueryWrapper<Water> waterLambdaQueryWrapper = new QueryWrapper<Water>().lambda();
        waterLambdaQueryWrapper.eq(Water::getHid,id);
        Water water = waterMapper.selectOne(waterLambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(water)){
            Double waterBalance = water.getAmount();
            houseViewVo.setWater(waterBalance);
        }


        //根据hid查询电费

        LambdaQueryWrapper<Electricity> electricityLambda = new QueryWrapper<Electricity>().lambda();
        electricityLambda.eq(Electricity::getHid,id);
        Electricity electricity = electricityMapper.selectOne(electricityLambda);
        if (ObjectUtils.isNotEmpty(electricity)){
            Double eleBalance = electricity.getAmount();
            houseViewVo.setEle(eleBalance);
        }

        return ResponseResult.success().data(houseViewVo);
    }




    @Override
    public ResponseResult houseImport(MultipartFile file) {

        if (file.isEmpty()){
            return ResponseResult.fail().message("请添加文件");
        }

        try {
            InputStream inputStream = file.getInputStream();
            ImportParams importParams = new ImportParams();
            importParams.setTitleRows(1);
            importParams.setHeadRows(1);
            List<House> houses = ExcelImportUtil.importExcel(inputStream, House.class, importParams);
            houses.forEach(house -> {
                String numbering = house.getNumbering();
                LambdaQueryWrapper<House> houseLambda = new QueryWrapper<House>().lambda();
                houseLambda.eq(House::getNumbering,numbering);
                House house1 = baseMapper.selectOne(houseLambda);
                if (ObjectUtils.isNotEmpty(house1)){
                    throw new HouseNoException();
                }
                baseMapper.insert(house);
            });
        } catch (HouseNoException e) {
            throw new HouseNoException();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseResult.success().message("批量导入成功");
    }
}




