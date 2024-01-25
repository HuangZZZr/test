package com.rms.backend.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rms.backend.common.ResponseResult;
import com.rms.backend.entity.Permission;
import com.rms.backend.entity.RolePers;
import com.rms.backend.service.PermissionService;
import com.rms.backend.service.RolePersService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @description: 权限
 * @author: huang
 * @date 2024年01月08日 17:27
 **/
@RestController
@RequestMapping("permission")
public class PermissionController {
    @Resource
    PermissionService permissionService;

    @Resource
    RolePersService rolePersService;

    //列表展示
    @GetMapping
    public ResponseResult getList(){
        List<Permission> list = permissionService.list();
        return ResponseResult.success().data(list);
    }



    //新增
    @PostMapping("insert")
    public ResponseResult addPermission(@RequestBody Permission permission){
        permissionService.save(permission);
        return ResponseResult.success().message("新增成功");
    }

    //根据菜单查询
    @GetMapping("search")
    public ResponseResult getPermissionByTitle(String title){
        LambdaQueryWrapper<Permission> lambda = new QueryWrapper<Permission>().lambda();
        lambda.eq(Permission::getTitle,title);
        List<Permission> list = permissionService.list(lambda);
        return ResponseResult.success().data(list);
    }

    @DeleteMapping("pid")
    public ResponseResult deletePermission(@PathVariable Integer pid){
        //传来的虽然是主键id，但主键id与子权限的pid对象，通过id=pid查询是否有子权限
        LambdaQueryWrapper<Permission> lambda = new QueryWrapper<Permission>().lambda();
        lambda.eq(Permission::getPid,pid);
        List<Permission> list = permissionService.list(lambda);
        if(list.size()>0){
            return ResponseResult.fail().message("有子权限不能删除");
        }
        //为空，删除per表和role-per表
        permissionService.removeById(pid);
        LambdaQueryWrapper<RolePers> queryWrapper = new QueryWrapper<RolePers>().lambda();
        queryWrapper.eq(RolePers::getPid,pid);
        rolePersService.remove(queryWrapper);
        return ResponseResult.success().message("删除成功");
    }
    //根据id查询回显数据
    @GetMapping("{pid}")
    public ResponseResult getPermissionById(@PathVariable Integer pid){

        Permission permission = permissionService.getById(pid);
        Integer pid1 = permission.getPid();
        String parentTitle = "";
        if (pid1!=0){
            parentTitle = permissionService.getById(pid1).getTitle();
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("permission",permission);
        result.put("parentTitle",parentTitle);
        return ResponseResult.success().data(result);
    }

}
