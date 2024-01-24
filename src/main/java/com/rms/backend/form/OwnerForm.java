package com.rms.backend.form;

import com.rms.backend.entity.Owner;
import lombok.Data;

import java.util.List;

@Data
public class OwnerForm extends Owner {

    private List<Integer> rids;
}
