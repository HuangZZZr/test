package com.rms.backend.common;

import lombok.Data;

/**
 * @description: 规范响应
 * @author: huang
 * @date 2023年12月02日 14:19
 **/
@Data
public class ResponseResult {
    private  String msg;
    private  Object data;
    private  Integer code;

    public static ResponseResult success(){
        ResponseResult result = new ResponseResult();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg(ResultEnum.SUCCESS.getMessage());
        return result;
    }

     public static  ResponseResult fail(){
         ResponseResult result = new ResponseResult();
         result.setCode(ResultEnum.FAIL.getCode());
         return result;
     }
     public ResponseResult  data(Object data){
        this.data = data;
        return  this;
    }
    public ResponseResult  message(String message){
        this.msg  = message;
        return  this;
    }

    public static ResponseResult error() {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(ResultEnum.ERROR.getCode());
        responseResult.setMsg(ResultEnum.ERROR.getMessage());
        return responseResult;
    }

    public ResponseResult code(Integer code) {
        this.code = code;
        return this;
    }

}
