package com.rms.backend.common;

import lombok.Data;

/**
 * @description:
 * @author: huang
 * @date 2024年01月22日 10:59
 **/
@Data
public class QueryCondition <T>{
    private Integer limit;
    private Integer page;
    private T query;
}
