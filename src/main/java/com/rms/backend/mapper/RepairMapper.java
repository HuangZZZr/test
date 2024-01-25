package com.rms.backend.mapper;

import com.rms.backend.entity.Repair;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 刘恒
* @description 针对表【repair】的数据库操作Mapper
* @createDate 2024-01-10 17:13:54
* @Entity com.rms.backend.entity.Repair
*/
public interface RepairMapper extends BaseMapper<Repair> {

    List<Repair> pepairData();
}




