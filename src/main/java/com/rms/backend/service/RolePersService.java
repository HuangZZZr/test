package com.rms.backend.service;

import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.RolePers;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author h'p
* @description 针对表【role_pers】的数据库操作Service
* @createDate 2024-01-19 15:00:40
*/
public interface RolePersService extends IService<RolePers> {

    ResponseResult addPermissions(Map<String,Object> rolePers);
}
