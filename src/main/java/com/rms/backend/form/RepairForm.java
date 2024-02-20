package com.rms.backend.form;

import com.rms.backend.entity.Repair;
import lombok.Data;

@Data
public class RepairForm extends Repair {

    private String account;

}
