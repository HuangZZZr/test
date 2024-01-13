package com.rms.backend.commons;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Projectname: rms-backend
 * @Filename: QueryCondition
 * @Author: LH
 * @Data:2024/1/10 20:36
 */
@Data
public class QueryCondition <T>{
    @ApiModelProperty(value = "当前页",example = "1")
    private Integer page;
    @ApiModelProperty(value = "每页显示的条数",example = "5")
    private Integer limit;
    @ApiModelProperty(value = "搜索条件",example = "{username:tom}")
    private T query;

}
