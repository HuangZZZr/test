package com.rms.backend.service.impl;

import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.*;
import com.rms.backend.form.LoginForm;
import com.rms.backend.form.PasswordForm;
import com.rms.backend.form.ProPertyForm;
import com.rms.backend.mapper.*;
import com.rms.backend.service.OwnerService;
import com.rms.backend.service.PropertyService;
import com.rms.backend.utils.IpUtil;
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
     private RolePersMapper rolePersMapper;
     @Resource
     private PermissionMapper permissionMapper;
     @Resource
    OwnerRoleMapper ownerRoleMapper;
     @Resource
     JWTUtil jwtUtil;

     @Resource
    StringRedisTemplate stringRedisTemplate;
    @Override
    public ResponseResult login(LoginForm loginForm,HttpServletRequest request) {
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
        lambda.eq(Property::getAccount,loginForm.getName());
        lam.eq(Owner::getUsername,loginForm.getName());
        //查询业主表
        Owner owner = ownerMapper.selectOne(lam);
        Property property = baseMapper.selectOne(lambda);

        //判断管理员表，如果有用户名则判断密码
        HashMap<String, Object> data = new HashMap<>();
        if(ObjectUtils.isNotNull(property)){
            if (property.getState()==1){
                return ResponseResult.fail().message("账户已被禁用");
            }
            //判断密码是否正确,数据库的密码进行了加密处理，需要将用户输入的密码进行加密
            String password = new Md5Hash(loginForm.getPassword(), property.getSalt(), 1024).toHex();
            if(!StringUtils.equals(property.getPassword(),password)){
                throw  new IncorrectCredentialsException();
            }
            //颁发令牌
            HashMap<String, Object> payload = new HashMap<>();
            payload.put("account",property.getAccount());
            payload.put("id",property.getId());
            token=jwtUtil.creteToken(payload);
            String ss = MD5.create().digestHex(token);
            String ip = IpUtil.getIp(request);
            stringRedisTemplate.opsForValue().set(ss,ip);
            data.put("account",property.getAccount());
            data.put("token",token);
        }//如果管理员表没有用户名，则判断业主表是否有用户名
        else if(ObjectUtils.isNotNull(owner)){
            if (owner.getStatue()==1){
                return ResponseResult.fail().message("账户已被禁用");
            }
            //如果不为空，判断密码是否正确
            String password=new Md5Hash(loginForm.getPassword(),owner.getSalt(),1024).toHex();
            if(!StringUtils.equals(owner.getPassword(),password)){
                throw  new IncorrectCredentialsException();
            }
            //颁发令牌
            HashMap<String, Object> ownPayload = new HashMap<>();
            ownPayload.put("account",owner.getUsername());
            ownPayload.put("id",owner.getId());
            //将payload封装进token
            token=jwtUtil.creteToken(ownPayload);
            String ss = MD5.create().digestHex(token);
            String ip = IpUtil.getIp(request);
            stringRedisTemplate.opsForValue().set(ss,ip);
            data.put("account",owner.getUsername());
            data.put("token",token);
        }//如果都没有，则抛出没有账户异常
        else {
            throw new AccountException();
        }
        return ResponseResult.success().data(data);
    }

    @Override
    public ResponseResult batchDelete(Integer id) {
        //首先根据id删除property表格的内容
        //然后根据id删除pro_role 表格的内容
        LambdaQueryWrapper<ProRole> lambda = new QueryWrapper<ProRole>().lambda();
           baseMapper.deleteById(id);
           lambda.in(ProRole::getPid,id);
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
        baseMapper.selectPage(page, lambda);
        List<Property> records = page.getRecords();
        long total = page.getTotal();
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageData",records);
        map.put("total",total);
        return ResponseResult.success().data(map);
    }

    @Override
    public ResponseResult editInfo(ProPertyForm proPertyForm, HttpServletRequest request) {
        //先从token获取name，查看是物业还是业主
        String token = request.getHeader("token");
        Map<String, Object> claim = jwtUtil.getClaims(token);
        //获取登录账户，property使用的是account，owner使用username
        String name = (String) claim.get("account");
        LambdaQueryWrapper<Property> lambda = new QueryWrapper<Property>().lambda();
        lambda.eq(Property::getAccount,name);
        Property property = baseMapper.selectOne(lambda);
        if(ObjectUtils.isNotNull(property)){
            Property pro = new Property();
            BeanUtils.copyProperties(proPertyForm,pro);
            System.out.println(pro.getSex());
            baseMapper.updateById(pro);
        }else {
            Owner owner = new Owner();
            BeanUtils.copyProperties(proPertyForm,owner);
            System.out.println(owner.getSex());
            ownerMapper.updateById(owner);
        }
        return ResponseResult.success().message("修改成功");
    }

    @Override
    public ResponseResult editPassword(PasswordForm passwordForm, HttpServletRequest request) {
        String token = request.getHeader("token");
        Map<String, Object> claim = jwtUtil.getClaims(token);
        String name = (String) claim.get("account");
        Integer id = (Integer) claim.get("id");
        LambdaQueryWrapper<Property> lambda = new QueryWrapper<Property>().lambda();
        lambda.eq(Property::getAccount,name);
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

    @Override
    public ResponseResult getPermissions(HttpServletRequest request) {
        String token = request.getHeader("token");
        Map<String, Object> claims = jwtUtil.getClaims(token);
        String account = (String) claims.get("account");

//        根据账号去俩张表查询
        Property property = baseMapper.selectOne(new QueryWrapper<Property>().lambda().eq(Property::getAccount, account));
        Owner owner = ownerMapper.selectOne(new QueryWrapper<Owner>().lambda().eq(Owner::getUsername, account));

        List<Permission> permissions = null;
        if (ObjectUtils.isNotEmpty(property)){
            List<Integer> roleIds = proRoleMapper.selectList(new QueryWrapper<ProRole>().lambda().eq(ProRole::getPid, property.getId()))
                    .stream().map(proRole -> proRole.getRid()).collect(Collectors.toList());
            List<Integer> pIds = rolePersMapper.selectList(new QueryWrapper<RolePers>().lambda().in(RolePers::getRid, roleIds))
                    .stream().map(rolePers -> rolePers.getPid()).collect(Collectors.toList());
            permissions = permissionMapper.selectList(new QueryWrapper<Permission>().lambda().in(Permission::getId, pIds))
                    .stream().filter(permission -> permission.getIsMenu() != 2)
                    .collect(Collectors.toList());
        }else if (ObjectUtils.isNotEmpty(owner)){
            List<Integer> roleIds = ownerRoleMapper.selectList(new QueryWrapper<OwnerRole>().lambda().eq(OwnerRole::getOid, owner.getId()))
                    .stream().map(proRole -> proRole.getRid()).collect(Collectors.toList());
            List<Integer> pIds = rolePersMapper.selectList(new QueryWrapper<RolePers>().lambda().in(RolePers::getRid, roleIds))
                    .stream().map(rolePers -> rolePers.getPid()).collect(Collectors.toList());
            permissions = permissionMapper.selectList(new QueryWrapper<Permission>().lambda().in(Permission::getId, pIds))
                    .stream().filter(permission -> permission.getIsMenu() != 2)
                    .collect(Collectors.toList());
        }

        return ResponseResult.success().data(permissions);
    }
}




