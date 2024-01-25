package com.rms.backend.controller;

import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Driveway;
import com.rms.backend.service.DrivewayService;
import com.rms.backend.service.OwnerDriService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("drive")
public class DriveWayController {

    @Resource
    private DrivewayService drivewayService;

    @Resource
    private OwnerDriService ownerDriService;

    @PostMapping("/list")
    public ResponseResult getDriveWayList(@RequestBody QueryCondition<Driveway> queryCondition){

    return drivewayService.getDriveWayList(queryCondition);
    }


}
