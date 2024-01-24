package com.rms.backend.commons;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("ResponseResult")
public class ResponseResult {
    @ApiModelProperty(value = "提示信息",example = "操作成功")
    private String message;
    @ApiModelProperty(value = "响应状态码",example = "0")
    private Integer code;
    @ApiModelProperty(value = "响应的页面数据",example = "{username:tom,password:123456}")
    private Object data;

    //操作成功
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
    public ResponseResult data(Object data){
        this.data = data;
        return this;
    }

    public ResponseResult code(Integer code){
        this.code = code;
        return this;
    }
}
