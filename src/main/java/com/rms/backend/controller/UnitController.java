package com.rms.backend.controller;

import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Unit;
import com.rms.backend.service.UnitService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("unit")
public class UnitController {
    @Resource
    private UnitService unitService;

    @PostMapping("list")
    public ResponseResult getUnitList (QueryCondition<Unit> queryCondition){
        return unitService.getUnitList(queryCondition);
    }
}
