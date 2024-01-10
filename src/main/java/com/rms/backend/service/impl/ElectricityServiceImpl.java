package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.entity.Electricity;
import com.rms.backend.service.ElectricityService;
import com.rms.backend.mapper.ElectricityMapper;
import org.springframework.stereotype.Service;

/**
* @author 刘恒
* @description 针对表【electricity】的数据库操作Service实现
* @createDate 2024-01-09 17:04:56
*/
@Service
public class ElectricityServiceImpl extends ServiceImpl<ElectricityMapper, Electricity>
    implements ElectricityService{

}




