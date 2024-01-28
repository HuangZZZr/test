package com.rms.backend.from;

import com.rms.backend.entity.Property;
import lombok.Data;

import java.util.List;

@Data
public class PropertyForm extends Property {
    private List<Integer> roleIds;
}
