package com.rms.backend.form;

import com.rms.backend.entity.Property;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: huang
 * @date 2024年01月24日 17:24
 **/
@Data
public class ProPertyForm extends Property {
        private List<Integer> roleIds;
}
