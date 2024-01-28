package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Permission;
import com.rms.backend.entity.ProRole;
import com.rms.backend.entity.Role;
import com.rms.backend.entity.RolePers;
import com.rms.backend.mapper.PermissionMapper;
import com.rms.backend.mapper.ProRoleMapper;
import com.rms.backend.mapper.RolePersMapper;
import com.rms.backend.service.RoleService;
import com.rms.backend.mapper.RoleMapper;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author h'p
* @description 针对表【role】的数据库操作Service实现
* @createDate 2024-01-08 17:25:09
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

    @Resource
    ProRoleMapper proRoleMapper;

    @Resource
    RolePersMapper rolePersMapper;

    @Resource
    PermissionMapper permissionMapper;
    @Override
    public ResponseResult delete(Integer id) {
        //先根据id查看pro-role表
        LambdaQueryWrapper<ProRole> queryWrapper = new QueryWrapper<ProRole>().lambda();
        queryWrapper.eq(ProRole::getPid,id);
        ProRole proRole = proRoleMapper.selectOne(queryWrapper);
        if(ObjectUtils.isNotNull(proRole)){
            return ResponseResult.fail().message("角色有人使用，无法删除");
        }
        baseMapper.deleteById(id);
        return ResponseResult.success().message("删除成功");
    }

    @Override
    public ResponseResult getPermissionsById(Integer id) {
        //根据id查询role-per表
        LambdaQueryWrapper<RolePers> queryWrapper = new QueryWrapper<RolePers>().lambda();
        queryWrapper.eq(RolePers::getRid,id);
        List<RolePers> rolePers = rolePersMapper.selectList(queryWrapper);
        //显示按钮级别的权限
        List<Integer> pids = rolePers.stream().map(rolePer -> rolePer.getPid()).collect(Collectors.toList());
        //通过pid查询权限表
        List<Integer> list = new ArrayList<>();
        if(pids.size()>0){
            list= permissionMapper.selectBatchIds(pids).stream()
                    .filter(p->p.getIsMenu()==2)
                    .map(p->p.getId()).collect(Collectors.toList());
        }
        return ResponseResult.success().data(list);
    }

    @Override
    public ResponseResult getRoleList(QueryCondition<Role> queryCondition) {
        Page<Role> rolePage = new Page<>(queryCondition.getPage(), queryCondition.getLimit());
        LambdaQueryWrapper<Role> lambda = new QueryWrapper<Role>().lambda();
        lambda.eq(StringUtils.isNotEmpty(queryCondition.getQuery().getRoleName()),Role::getRoleName,queryCondition.getQuery().getRoleName());
        baseMapper.selectPage(rolePage,lambda);
        List<Role> records = rolePage.getRecords();
        long total = rolePage.getTotal();
        Map<String, Object> map = new HashMap<>();
        map.put("pageData",records);
        map.put("total",total);
        return ResponseResult.success().data(map);
    }
}




