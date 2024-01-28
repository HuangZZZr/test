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
import java.util.Set;
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
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }
    //权限控制
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Integer id = (Integer) principalCollection.getPrimaryPrincipal();//会获取new SimpleAuthenticationInfo(uid, tk, this.getName());第一个参数
        //先查询pro-role表，获得rid
        LambdaQueryWrapper<ProRole> lambda = new QueryWrapper<ProRole>().lambda();
        lambda.eq(ProRole::getPid,id);
        List<ProRole> proRoles = proRoleMapper.selectList(lambda);

        List<Integer> rids = proRoles.stream().map(proRole -> proRole.getRid()).collect(Collectors.toList());

        //        根据用户角色rid查询权限pid
        Set<Integer> pIds = rolePersMapper.selectList(new QueryWrapper<RolePers>().lambda().in(RolePers::getRid, rids))
                .stream().map(rp -> rp.getPid()).collect(Collectors.toSet());
//        根据pid查询permission权限字符串->选择isMenu==2的按钮权限
        List<String> permissions = permissionMapper.selectList(new QueryWrapper<Permission>().lambda().in(Permission::getId, pIds))
                .stream().filter(p -> p.getIsMenu() == 2)
                .map(p -> p.getPermission()).collect(Collectors.toList());

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addStringPermissions(permissions);
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
        Object uid = claim.get("id");

        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(uid, tk, this.getName());

        return simpleAuthenticationInfo;

    }
}
