package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.entity.Property;
import com.rms.backend.service.PropertyService;
import com.rms.backend.mapper.PropertyMapper;
import org.springframework.stereotype.Service;

/**
* @author 16600
* @description 针对表【property】的数据库操作Service实现
* @createDate 2024-01-24 13:39:40
*/
@Service
public class PropertyServiceImpl extends ServiceImpl<PropertyMapper, Property>
    implements PropertyService{

}




