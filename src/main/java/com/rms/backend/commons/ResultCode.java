package com.rms.backend.commons;

public enum ResultCode {
    SUCCESS(0,"操作成功"),
    FAIL(110,"操作失败"),
    NO_AUTHENTICATED(401,"请认证后访问"),
    ERROR(500,"异常发生，请及时排查错误")
    ;
    private String message;
    private Integer code;

    ResultCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public Integer getCode() {
        return this.code;
    }
}
