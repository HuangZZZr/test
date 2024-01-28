package com.rms.backend.shiro;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.rms.backend.commons.ResponseResult;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * @description: jwt拦截器
 * @author: huang
 * @date 2024年01月08日 17:36
 **/

public class JWTFilter extends BasicHttpAuthenticationFilter {
    //请求的预处理，跨域会发送一个预请求
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));

        // 跨域时会首先发送一个option请求，option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }


    //判断请求是否允许，如果为true，则进入controller
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        try {
            executeLogin(request,response);
        } catch (Exception e) {
            //如果发生异常,不会走全局异常类，需要把错误信息写成流返回前端
              response.setContentType("application/json;charset=utf-8");//设置返回格式
            PrintWriter writer=null;
            try {
                writer=response.getWriter();
                String result = JSONUtil.toJsonStr(ResponseResult.error().code(401).message("请求未认证,请认证后访问...."));
                writer.write(result);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }finally {
                writer.close();
            }


        }
        return super.isAccessAllowed(request, response, mappedValue);
    }

    //验证
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
      //从request中获取token
      HttpServletRequest req= (HttpServletRequest) request;
        String token = req.getHeader("token");

        //判断token是否为空
        if(StringUtils.isEmpty(token)){
             throw  new RuntimeException("请认证后访问");
        }
        //验证token真伪
        Subject subject = getSubject(request, response);

        // 需要AuthenticationToken类型

        JwtToken jwtToken = new JwtToken(token);

        subject.login(jwtToken);

        return super.executeLogin(request, response);
    }
}
