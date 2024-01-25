package com.rms.backend.mapper;

import com.rms.backend.entity.Driveway;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 刘恒
* @description 针对表【driveway】的数据库操作Mapper
* @createDate 2024-01-10 17:13:54
* @Entity com.rms.backend.entity.Driveway
*/
public interface DrivewayMapper extends BaseMapper<Driveway> {

    List<Driveway> drivewayData();
}




