package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.entity.Permission;
import com.rms.backend.service.PermissionService;
import com.rms.backend.mapper.PermissionMapper;
import org.springframework.stereotype.Service;

/**
* @author h'p
* @description 针对表【permission】的数据库操作Service实现
* @createDate 2024-01-08 17:25:09
*/
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission>
    implements PermissionService{

}




