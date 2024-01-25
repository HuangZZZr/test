package com.rms.backend.service;

import com.rms.backend.common.QueryCondition;
import com.rms.backend.common.ResponseResult;
import com.rms.backend.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.models.auth.In;

import java.util.List;

/**
* @author h'p
* @description 针对表【role】的数据库操作Service
* @createDate 2024-01-08 17:25:09
*/
public interface RoleService extends IService<Role> {

    ResponseResult delete(Integer[] id);

    ResponseResult getPermissionsById(Integer id);

    ResponseResult getRoleList(QueryCondition<Role> queryCondition);
}
