package com.rms.backend.aspects;

import cn.hutool.json.JSONUtil;
import com.rms.backend.commons.Logs;
import com.rms.backend.entity.LogInfo;
import com.rms.backend.service.LogInfoService;
import com.rms.backend.utils.IpUtil;
import com.rms.backend.utils.JWTUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

@Aspect
@Component
public class LogAspect {

    @Resource
    private LogInfoService logInfoService;

    @Resource
    private JWTUtil jwtUtil;

    @Pointcut("@annotation(com.rms.backend.commons.Logs)")
    public void pointCut(){};

    //记录日志
    @Around("pointCut()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed = null;

        long start = System.currentTimeMillis();

        //目标执行方法
        proceed = joinPoint.proceed();
       long end =  System.currentTimeMillis();
       Integer time = (int)(end-start);

       //获取参数
        Object[] args = joinPoint.getArgs();
        String str = JSONUtil.toJsonStr(args);

        MethodSignature signature=(MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();
        Logs annotation = method.getAnnotation(Logs.class);
        String model = annotation.model();
        String operationInfo = annotation.operation().getOperationInfo();

        //获取对象的请求
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String ip = IpUtil.getIp(request);
        String requestURI = request.getRequestURI();

        String token = request.getHeader("token");
        Map<String, Object> claims = jwtUtil.getClaims(token);
        String name = (String) claims.get("name");

        //封装数据，将数据写进日志表
        LogInfo logInfo = new LogInfo();
        logInfo.setIp(ip);
        logInfo.setName(name);
        logInfo.setModel(model);
        logInfo.setParams(str);
        logInfo.setUrl(requestURI);
        logInfo.setDescription(operationInfo);
        logInfo.setTimeConsuming(time);
        logInfoService.save(logInfo);

        return proceed;
    }


}
