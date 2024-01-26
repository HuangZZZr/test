package com.rms.backend.controller;


import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.rms.backend.common.QueryCondition;
import com.rms.backend.common.ResponseResult;
import com.rms.backend.entity.Owner;
import com.rms.backend.entity.ProRole;
import com.rms.backend.entity.Property;
import com.rms.backend.form.LoginForm;
import com.rms.backend.form.PasswordForm;
import com.rms.backend.form.ProPertyForm;
import com.rms.backend.service.OwnerService;
import com.rms.backend.service.ProRoleService;
import com.rms.backend.service.PropertyService;
import com.rms.backend.shiro.JwtToken;
import com.rms.backend.utils.JWTUtil;
import com.rms.backend.utils.SaltUtil;
import io.swagger.models.auth.In;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;

/**
 * @description: 管理员
 * @author: huang
 * @date 2024年01月08日 17:26
 **/
@RestController
@RequestMapping("property")
public class PropertyController {
    @Resource
    PropertyService propertyService;
    @Resource
    OwnerService ownerService;

    @Resource
    ProRoleService proRoleService;

    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    JWTUtil jwtUtil;
    //登录
    @PostMapping("login")
    public ResponseResult login(@RequestBody LoginForm loginForm,HttpServletRequest request){
        return  propertyService.login(loginForm,request);
    }

    //列表展示
    @PostMapping ("list")
    public ResponseResult propertyList(@RequestBody QueryCondition<Property> queryCondition){
        return  propertyService.getList(queryCondition);
    }

    //修改或新增
    @PostMapping("saveOrUpdatePropertyInfo")
    public ResponseResult saveOrUpdatePropertyInfo(@RequestBody ProPertyForm property){
        //查询名字是否重复 property和owner表
        LambdaQueryWrapper<Property> propertyLambda = new QueryWrapper<Property>().lambda();
        LambdaQueryWrapper<Owner> lambda = new QueryWrapper<Owner>().lambda();
        propertyLambda.eq(Property::getName,property.getName());
        lambda.eq(Owner::getName,property.getName());
        Property pro = propertyService.getOne(propertyLambda);
        Owner owner =ownerService.getOne(lambda);
        if(ObjectUtils.isNotNull(pro)||ObjectUtils.isNotNull(owner)){
            return ResponseResult.fail().message("用户名重复");
        }
        propertyService.saveOrUpdate(property);
        //先通过id删除pro-role表的相信息。
        LambdaQueryWrapper<ProRole> queryWrapper = new QueryWrapper<ProRole>().lambda();
        queryWrapper.eq(ProRole::getPid,property.getId());
        proRoleService.remove(queryWrapper);
        //添加 foreach不需要返回值
        property.getRoleIds().forEach(rid->{
            ProRole proRole = new ProRole();
            proRole.setPid(property.getId());
            proRole.setRid(rid);
            proRoleService.save(proRole);
        });
       return ResponseResult.success().message("操作成功");
    }

    //修改状态
    @PostMapping("state")
    public ResponseResult editPropertyState(@RequestBody Property property){
        propertyService.updateById(property);
        return ResponseResult.success().message("修改成功");
    }

    //获取property信息，用于回显
    @GetMapping("getInfo/{id}")
    public ResponseResult getInfoById(@PathVariable Integer id){
        Property property = propertyService.getById(id);
      return   ResponseResult.success().data(property);
    }

    //删除property信息
    @DeleteMapping
    public ResponseResult deleteByIds(@RequestBody Integer[] ids){
        return  propertyService.batchDelete(Arrays.asList(ids));
    }

    //重置密码
    @PutMapping("resetPwd/{id}")
    public ResponseResult resetPwd(@PathVariable Integer id){
        Property property = new Property();
        property.setId(id);
        String salt = SaltUtil.createSalt(8);
        property.setSalt(salt);
        String toHex = new Md5Hash("666666", salt, 1024).toHex();
        property.setPassword(toHex);
        propertyService.updateById(property);
        return ResponseResult.success().message("修改密码成功");
    }

    //获取物业或业主信息
    @GetMapping("getInfo")
    public ResponseResult getInfo(HttpServletRequest request){
        String token = request.getHeader("token");
        Map<String, Object> claim = jwtUtil.getClaims(token);
        String name = (String) claim.get("name");
        LambdaQueryWrapper<Property> lambda = new QueryWrapper<Property>().lambda();
        LambdaQueryWrapper<Owner> queryWrapper = new QueryWrapper<Owner>().lambda();
        queryWrapper.eq(Owner::getName,name);
        lambda.eq(Property::getName,name);
        Property one = propertyService.getOne(lambda);
        if(ObjectUtils.isNotNull(one)) {
             return ResponseResult.success().data(one);
        }else {
            Owner owner = ownerService.getOne(queryWrapper);
            return ResponseResult.success().data(owner);
        }
    }

    //修改个人信息
     @PostMapping("editInfo")
     public ResponseResult editInfo(@RequestBody ProPertyForm proPertyForm,HttpServletRequest request){
         return   propertyService.editInfo(proPertyForm,request);

     }

    //修改个人密码
     @PostMapping("editPassword")
     public ResponseResult editPassword(@RequestBody PasswordForm passwordForm,HttpServletRequest request){

        return propertyService.editPassword(passwordForm,request);
     }

    //安全退出
    @GetMapping("logout")
    public ResponseResult logout(HttpServletRequest request){
        String token = request.getHeader("token");
        stringRedisTemplate.delete(MD5.create().digestHex(token));
        return ResponseResult.success().message("安全退出");
    }



}
