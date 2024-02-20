package com.rms.backend.form;

import com.rms.backend.entity.Complain;
import lombok.Data;

@Data
public class ComplainForm extends Complain {
    private String account;
}
