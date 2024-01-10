package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.entity.House;
import com.rms.backend.service.HouseService;
import com.rms.backend.mapper.HouseMapper;
import org.springframework.stereotype.Service;

/**
* @author 刘恒
* @description 针对表【house】的数据库操作Service实现
* @createDate 2024-01-09 17:04:56
*/
@Service
public class HouseServiceImpl extends ServiceImpl<HouseMapper, House>
    implements HouseService{

}




