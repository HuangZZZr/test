package com.rms.backend.form;

import lombok.Data;

/**
 * @description:
 * @author: huang
 * @date 2024年01月24日 17:49
 **/
@Data
public class PasswordForm {
    private   String newPassword;
    private  String oldPassword;
}
