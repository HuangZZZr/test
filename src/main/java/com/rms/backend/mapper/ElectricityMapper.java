package com.rms.backend.mapper;

import com.rms.backend.entity.Electricity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 刘恒
* @description 针对表【electricity】的数据库操作Mapper
* @createDate 2024-01-09 17:04:56
* @Entity com.rms.backend.entity.Electricity
*/
public interface ElectricityMapper extends BaseMapper<Electricity> {

    List<Electricity> electricityData();
}




