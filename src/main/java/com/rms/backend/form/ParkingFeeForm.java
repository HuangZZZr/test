package com.rms.backend.form;

import com.rms.backend.entity.ParkingFree;
import lombok.Data;

@Data
public class ParkingFeeForm extends ParkingFree {
    private String account;
    private String nos;
}
