package com.rms.backend.controller;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.rms.backend.common.QueryCondition;
import com.rms.backend.common.ResponseResult;
import com.rms.backend.entity.Role;
import com.rms.backend.entity.RolePers;
import com.rms.backend.service.RolePersService;
import com.rms.backend.service.RoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @description: 角色
 * @author: huang
 * @date 2024年01月08日 17:27
 **/
@RestController
@RequestMapping("role")
public class RoleController {
    @Resource
    RoleService roleService;
    @Resource
    RolePersService rolePersService;
    //展示
    @PostMapping("list")
    public ResponseResult getList(@RequestBody QueryCondition<Role> queryCondition){
          return  roleService.getRoleList(queryCondition);
    }
    //新增或修改
    @PostMapping("saveOrUpdateRoleInfo")
    public ResponseResult saveOrUpdateRoleInfo(@RequestBody Role role){
        //角色不能重复
        Role role1 = roleService.getById(role.getId());
        if (ObjectUtils.isNotNull(role1)){
            return ResponseResult.fail().message("角色名重复");
        }
        roleService.saveOrUpdate(role);
        return ResponseResult.success().message("添加成功");
    }

    //删除
    @DeleteMapping("{id}")
    public ResponseResult delete(@PathVariable Integer[] id){
        return  roleService.delete(id);
    }

    //根据id回显
    @GetMapping("{id}")
    public ResponseResult getById(@PathVariable Integer id){
        Role role = roleService.getById(id);
        return ResponseResult.success().data(role);
    }
    //根据id查询权限
    @GetMapping("permissions/{id}")
     public ResponseResult getPermissionsById(@PathVariable Integer id){
        return roleService.getPermissionsById(id);
    }

    //新增用户权限
    @PostMapping("addPermissions")
    public ResponseResult addPermissions(@RequestBody Map<String,Object> rolePers){
        return rolePersService.addPermissions(rolePers);
    }

    //角色下拉选项
    @GetMapping("search")
    public ResponseResult roleSelect(){
        List<Role> list = roleService.list();
        return ResponseResult.success().data(list);
    }


}
