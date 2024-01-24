package com.rms.backend.aspects;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.rms.backend.entity.LoginInfo;
import com.rms.backend.service.LoginInfoService;
import com.rms.backend.utils.IpUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

@Component
@Aspect
public class LoginAspect {

    @Resource
    private LoginInfoService loginInfoService;

    @Pointcut("@annotation(com.rms.backend.commons.Logins)")
    public void pointCut(){};

    @Around("pointCut()")
    public  Object aroundLogin(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object proceed = null;

        proceed = proceedingJoinPoint.proceed();

        //获取参数
        Object[] args = proceedingJoinPoint.getArgs();
        String str = JSONUtil.toJsonStr(args);

        JSONArray jsonArray = JSONUtil.parseArray(str);
        Map<String,Object> map = (Map<String, Object>) jsonArray.get(0);
        String name = (String) map.get("name");

        //获取用户ip
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String ip = IpUtil.getIp(request);

        //封装数据，将数据写进登陆日志表
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setName(name);
        loginInfo.setIp(ip);

        loginInfoService.save(loginInfo);


        return proceed;
    }
}
