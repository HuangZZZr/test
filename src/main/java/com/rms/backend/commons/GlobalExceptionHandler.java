package com.rms.backend.commons;


import com.rms.backend.exceptions.HouseNoException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Projectname: rms-backend
 * @Filename: GlobalExceptionHandler
 * @Author: LH
 * @Data:2024/1/12 15:53
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HouseNoException.class)
    public ResponseResult otherExceptionHandler(HouseNoException e){
        e.printStackTrace();
        return ResponseResult.error().message("房屋编号有重复");
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult otherExceptionHandler(Exception e){
        e.printStackTrace();
        return ResponseResult.error();
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseResult authorizationExceptionHandler(AuthorizationException e){
        e.printStackTrace();
        return ResponseResult.error().code(403).message("你没有权限进行该操作，请联系管理员");
    }
}
