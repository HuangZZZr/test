package com.rms.backend.shiro;

import cn.hutool.crypto.digest.MD5;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.rms.backend.entity.Permission;
import com.rms.backend.entity.ProRole;
import com.rms.backend.entity.RolePers;
import com.rms.backend.exception.TokenTheftException;
import com.rms.backend.mapper.PermissionMapper;
import com.rms.backend.mapper.ProRoleMapper;
import com.rms.backend.mapper.RolePersMapper;
import com.rms.backend.utils.IpUtil;
import com.rms.backend.utils.JWTUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description: 自定义realm
 * @author: huang
 * @date 2024年01月08日 17:36
 **/

public class CustomerRealm extends AuthorizingRealm {

    @Resource
    ProRoleMapper proRoleMapper;

    @Resource
    RolePersMapper rolePersMapper;

    @Resource
    PermissionMapper permissionMapper;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    JWTUtil jwtUtil;
    //权限控制
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Integer id = (Integer) principalCollection.getPrimaryPrincipal();//会获取new SimpleAuthenticationInfo(uid, tk, this.getName());第一个参数
        //先查询pro-role表，获得rid
        LambdaQueryWrapper<ProRole> lambda = new QueryWrapper<ProRole>().lambda();
        lambda.eq(ProRole::getPid,id);
        List<ProRole> proRoles = proRoleMapper.selectList(lambda);

        //遍历集合，查询perId
        List<RolePers> list = proRoles.stream().map(proRole -> {
            LambdaQueryWrapper<RolePers> wrapper = new QueryWrapper<RolePers>().lambda();
            wrapper.eq(RolePers::getRid, proRole.getRid());
            RolePers rolePers = rolePersMapper.selectOne(wrapper);
            return rolePers;
        }).collect(Collectors.toList());

        //遍历list，获取每个perId对应的per对象
        List<Permission> permissions = list.stream().map(p -> {
            Permission permission = permissionMapper.selectById(p.getPid());
            return permission;
        }).collect(Collectors.toList());
        List<String> c = permissions.stream().map(permission -> permission.getPermission()).collect(Collectors.toList());
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addStringPermissions(c);
        return simpleAuthorizationInfo;
    }
    //token验证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
           
        JwtToken token= (JwtToken) authenticationToken;
        String tk = (String) token.getPrincipal();
        //判断redis是否有token
        if (!stringRedisTemplate.hasKey(MD5.create().digestHex(tk))) {
            throw new TokenExpiredException("token被移除了");
        }

        // 2. token校验  是否被盗用
        String redisIp = stringRedisTemplate.opsForValue().get(MD5.create().digestHex(tk));
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = requestAttributes.getRequest();
        String ip = IpUtil.getIp(req);
        if (!StringUtils.equals(redisIp, ip)) {
            throw new TokenTheftException();
        }
        //验证token真伪
        jwtUtil.verifyToken(tk);

        //从payload里获取uid

        Map<String, Object> claim = jwtUtil.getClaims(tk);
        String uid = (String) claim.get("uid");

        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(uid, tk, this.getName());

        return simpleAuthenticationInfo;

    }
}
