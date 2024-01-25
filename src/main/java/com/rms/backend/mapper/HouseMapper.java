package com.rms.backend.mapper;

import com.rms.backend.entity.House;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
* @author 刘恒
* @description 针对表【house】的数据库操作Mapper
* @createDate 2024-01-10 17:13:54
* @Entity com.rms.backend.entity.House
*/
public interface HouseMapper extends BaseMapper<House> {

    List<Map<Integer, Integer>> houseData();
}




