package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.entity.Role;
import com.rms.backend.service.RoleService;
import com.rms.backend.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
* @author 16600
* @description 针对表【role】的数据库操作Service实现
* @createDate 2024-01-24 13:39:40
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

}




