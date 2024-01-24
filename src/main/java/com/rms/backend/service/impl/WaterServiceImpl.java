package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.entity.Water;
import com.rms.backend.service.WaterService;
import com.rms.backend.mapper.WaterMapper;
import org.springframework.stereotype.Service;

/**
* @author 16600
* @description 针对表【water】的数据库操作Service实现
* @createDate 2024-01-22 10:41:58
*/
@Service
public class WaterServiceImpl extends ServiceImpl<WaterMapper, Water>
    implements WaterService{

}




