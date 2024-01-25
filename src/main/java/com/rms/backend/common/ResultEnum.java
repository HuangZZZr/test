package com.rms.backend.common;

public enum ResultEnum {

    SUCCESS(0,"操作成功"),
    FAIL(110,"操作失败"),
    NO_AUTHENTICATED(401,"请认证后访问"),
    ERROR(500,"服务器异常,请刷新重试!!!");

    private Integer code;
    private String message;

    ResultEnum(Integer code,String message){
        this.code = code;
        this.message = message;
    }

    public String getMessage(){
        return  this.message;
    }

    public Integer getCode(){
        return  this.code;
    }

}
