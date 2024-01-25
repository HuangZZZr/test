package com.rms.backend.vo;

import com.rms.backend.entity.Property;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: huang
 * @date 2024年01月22日 10:54
 **/
@Data
public class PropertyVo extends Property {
    private List<String> roleNames;
}
