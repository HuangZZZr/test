package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.entity.Owner;
import com.rms.backend.service.OwnerService;
import com.rms.backend.mapper.OwnerMapper;
import org.springframework.stereotype.Service;

/**
* @author 刘恒
* @description 针对表【owner】的数据库操作Service实现
* @createDate 2024-01-09 17:04:56
*/
@Service
public class OwnerServiceImpl extends ServiceImpl<OwnerMapper, Owner>
    implements OwnerService{

}




