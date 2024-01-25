package com.rms.backend.form;

import lombok.Data;

/**
 * @description: 登录菜单
 * @author: huang
 * @date 2024年01月10日 20:35
 **/
@Data
public class LoginForm {
    private  String name;
    private  String password;
    private  String imgUuid;
    private  String code;
}
