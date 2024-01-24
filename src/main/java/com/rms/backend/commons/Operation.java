package com.rms.backend.commons;



public enum Operation {

    ADD("添加"),
    DELETE("删除"),
    UPDATE("修改"),
    SELECT("查询"),
    LOGIN("登录"),
    OTHER("其他");

    private String operationInfo;

    Operation(String operationInfo){
        this.operationInfo = operationInfo;
    }

    public String getOperationInfo(){
        return this.operationInfo;
    }
}
