package com.rms.backend.commons;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ResponseResult {
    private String message;
    private Integer code;
    private Object data;

    public static ResponseResult success(){
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(ResultCode.SUCCESS.getCode());
        responseResult.setMessage(ResultCode.SUCCESS.getMessage());
        return responseResult;
    }

    public static ResponseResult fail(){
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(ResultCode.FAIL.getCode());
        responseResult.setMessage(ResultCode.FAIL.getMessage());
        return responseResult;
    }

    public static ResponseResult error(){
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(ResultCode.ERROR.getCode());
        responseResult.setMessage(ResultCode.ERROR.getMessage());
        return responseResult;
    }

    public ResponseResult message(String message){
        this.message = message;
        return this;
    }
    public ResponseResult code(Integer code){
        this.code = code;
        return this;
    }
    public ResponseResult data(Object data){
        this.data = data;
        return this;
    }
}
