package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.common.QueryCondition;
import com.rms.backend.common.ResponseResult;
import com.rms.backend.entity.*;
import com.rms.backend.form.LoginForm;
import com.rms.backend.form.PasswordForm;
import com.rms.backend.form.ProPertyForm;
import com.rms.backend.mapper.*;
import com.rms.backend.service.OwnerService;
import com.rms.backend.service.PropertyService;
import com.rms.backend.utils.JWTUtil;
import com.rms.backend.utils.SaltUtil;
import com.rms.backend.vo.PropertyVo;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author h'p
* @description 针对表【property】的数据库操作Service实现
* @createDate 2024-01-08 17:25:09
*/
@Service
public class PropertyServiceImpl extends ServiceImpl<PropertyMapper, Property>
    implements PropertyService{

     @Resource
    OwnerMapper ownerMapper;
     @Resource
    ProRoleMapper proRoleMapper;
     @Resource
     RoleMapper roleMapper;

     @Resource
    OwnerRoleMapper ownerRoleMapper;
     @Resource
     JWTUtil jwtUtil;

     @Resource
    StringRedisTemplate stringRedisTemplate;
    @Override
    public ResponseResult login(LoginForm loginForm) {
        //先判断验证码是否过期
        String s = stringRedisTemplate.opsForValue().get(loginForm.getImgUuid());
        if(StringUtils.isEmpty(s)){
            return ResponseResult.fail().message("验证码已过期,请刷新");
        }
        if(!StringUtils.equals(s,loginForm.getCode())){
            return ResponseResult.fail().message("验证码错误");
        }

        //创建token
        String token;
        //比较用户名，分别比较两个表
        LambdaQueryWrapper<Property> lambda = new QueryWrapper<Property>().lambda();
        LambdaQueryWrapper<Owner> lam = new QueryWrapper<Owner>().lambda();
        lambda.eq(Property::getName,loginForm.getName());
        lam.eq(Owner::getName,loginForm.getName());
        //查询业主表
        Owner owner = ownerMapper.selectOne(lam);
        Property property = baseMapper.selectOne(lambda);
        //判断管理员表，如果有用户名则判断密码
        if(ObjectUtils.isNotNull(property)){
            //判断密码是否正确,数据库的密码进行了加密处理，需要将用户输入的密码进行加密
            String password = new Md5Hash(loginForm.getPassword(), property.getSalt(), 1024).toHex();
            if(!StringUtils.equals(property.getPassword(),password)){
                throw  new IncorrectCredentialsException();
            }
            //颁发令牌
            HashMap<String, Object> payload = new HashMap<>();
            payload.put("name",property.getName());
            payload.put("id",property.getId());
           token=jwtUtil.creteToken(payload);
        }//如果管理员表没有用户名，则判断业主表是否有用户名
        else if(ObjectUtils.isNotNull(owner)){
            //如果不为空，判断密码是否正确
            String password=new Md5Hash(loginForm.getPassword(),property.getSalt(),1024).toHex();
            if(!StringUtils.equals(property.getPassword(),password)){
                throw  new IncorrectCredentialsException();
            }
            //颁发令牌
            HashMap<String, Object> ownPayload = new HashMap<>();
            ownPayload.put("name",owner.getName());
            ownPayload.put("id",owner.getId());
            //将payload封装进token
            token=jwtUtil.creteToken(ownPayload);
        }//如果都没有，则抛出没有账户异常
        else {
            throw new AccountException();
        }
        return ResponseResult.success().data(token);
    }


    @Override
    public ResponseResult batchDelete(List<Integer> asList) {
        //首先根据id删除property表格的内容
        //然后根据id删除pro_role 表格的内容
        LambdaQueryWrapper<ProRole> lambda = new QueryWrapper<ProRole>().lambda();
           baseMapper.deleteBatchIds(asList);
           lambda.in(ProRole::getPid,asList);
           proRoleMapper.delete(lambda);
        return ResponseResult.success().message("删除成功");
    }

    @Override
    public ResponseResult getList(QueryCondition<Property> queryCondition) {
        //分页对象
        Page<Property> page = new Page<>(queryCondition.getPage(), queryCondition.getLimit());
        LambdaQueryWrapper<Property> lambda = new QueryWrapper<Property>().lambda();
        lambda.eq(StringUtils.isNotEmpty(queryCondition.getQuery().getAccount()),Property::getAccount,queryCondition.getQuery().getAccount())
                .eq(ObjectUtils.isNotNull(queryCondition.getQuery().getState()),Property::getState,queryCondition.getQuery().getState())
                .eq(ObjectUtils.isNotNull(queryCondition.getQuery().getServing()),Property::getServing,queryCondition.getQuery().getServing());
        baseMapper.selectList(page, lambda);
        List<Property> records = page.getRecords();
        //遍历集合
        List<PropertyVo> collect = records.stream().map(property -> {
            PropertyVo propertyVo = new PropertyVo();
            //复制属性
            BeanUtils.copyProperties(property, propertyVo);
            LambdaQueryWrapper<ProRole> queryWrapper = new QueryWrapper<ProRole>().lambda();
            queryWrapper.eq(ProRole::getPid, property.getId());
            List<ProRole> proRoles = proRoleMapper.selectList(queryWrapper);
            List<Integer> rid = proRoles.stream().map(proRole -> proRole.getRid()).collect(Collectors.toList());
            List<String> roles = roleMapper.selectBatchIds(rid).stream().map(role -> role.getRoleName()).collect(Collectors.toList());
            propertyVo.setRoleNames(roles);
            return propertyVo;
        }).collect(Collectors.toList());
        long total = page.getTotal();
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageData",collect);
        map.put("total",total);
        return ResponseResult.success().data(map);
    }

    @Override
    public ResponseResult editInfo(ProPertyForm proPertyForm, HttpServletRequest request) {
        //先从token获取name，查看是物业还是业主
        String token = request.getHeader("token");
        Map<String, Object> claim = jwtUtil.getClaim(token);
        String name = (String) claim.get("name");
        LambdaQueryWrapper<Property> lambda = new QueryWrapper<Property>().lambda();
        lambda.eq(Property::getName,name);
        Property property = baseMapper.selectOne(lambda);
        if(ObjectUtils.isNotNull(property)){
            Property pro = new Property();
            BeanUtils.copyProperties(proPertyForm,pro);
            baseMapper.updateById(pro);
            //先删除关联表
            LambdaQueryWrapper<ProRole> queryWrapper = new QueryWrapper<ProRole>().lambda();
            queryWrapper.eq(ProRole::getPid,proPertyForm.getId());
            proRoleMapper.delete(queryWrapper);
            //循环添加
            proPertyForm.getRoleIds().forEach(rid->{
                ProRole proRole = new ProRole();
                proRole.setPid(proPertyForm.getId());
                proRole.setRid(rid);
                proRoleMapper.insert(proRole);
            });
        }else {
            Owner owner = new Owner();
            BeanUtils.copyProperties(proPertyForm,owner);
            LambdaQueryWrapper<OwnerRole> queryWrapper = new QueryWrapper<OwnerRole>().lambda();
            queryWrapper.eq(OwnerRole::getOid,owner.getId());
            ownerRoleMapper.delete(queryWrapper);
            proPertyForm.getRoleIds().forEach(rid->{
                OwnerRole ownerRole = new OwnerRole();
                ownerRole.setOid(owner.getId());
                ownerRole.setRid(rid);
            });
        }
        return ResponseResult.success().message("修改成功");
    }

    @Override
    public ResponseResult editPassword(PasswordForm passwordForm, HttpServletRequest request) {
        String token = request.getHeader("token");
        Map<String, Object> claim = jwtUtil.getClaim(token);
        String name = (String) claim.get("name");
        Integer id = (Integer) claim.get("id");
        LambdaQueryWrapper<Property> lambda = new QueryWrapper<Property>().lambda();
        lambda.eq(Property::getName,name);
        Property property = baseMapper.selectOne(lambda);
        if(ObjectUtils.isNotNull(property)){
            // 1. 判定原始密码是否正确  明文
            String oldPassword = passwordForm.getOldPassword();
            String dataBasePassword = property.getPassword();
            String salt = property.getSalt();
            String userPassword = new Md5Hash(oldPassword, salt, 1024).toHex();
            if (!StringUtils.equals(dataBasePassword, userPassword)) {
                return ResponseResult.fail().message("原始密码不正确");
            }
            // 更新密码
            String newPassword = passwordForm.getNewPassword();
            String salt1 = SaltUtil.createSalt(8);
            String newHex = new Md5Hash(newPassword, salt1, 1024).toHex();
            property.setPassword(newHex);
            property.setSalt(salt1);
            baseMapper.updateById(property);
        }else {
            Owner owner = ownerMapper.selectById(id);
            String oldPassword = passwordForm.getOldPassword();
            String dataBasePassword = owner.getPassword();
            String salt = owner.getSalt();
            String userPassword = new Md5Hash(oldPassword, salt, 1024).toHex();
            if (!StringUtils.equals(dataBasePassword, userPassword)) {
                return ResponseResult.fail().message("原始密码不正确");
            }
            // 更新密码
            String newPassword = passwordForm.getNewPassword();
            String salt1 = SaltUtil.createSalt(8);
            String newHex = new Md5Hash(newPassword, salt1, 1024).toHex();
            owner.setPassword(newHex);
            owner.setSalt(salt1);
            ownerMapper.updateById(owner);
        }
        return ResponseResult.success().message("密码更新成功");
    }
}




