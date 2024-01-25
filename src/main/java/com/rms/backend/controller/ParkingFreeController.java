package com.rms.backend.controller;

import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.ParkingFree;
import com.rms.backend.service.ParkingFreeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("park")
public class ParkingFreeController {

    @Resource
    private ParkingFreeService parkingFreeService;

    @PostMapping("/list")
    public ResponseResult getParkingFree (@RequestBody QueryCondition<ParkingFree> queryCondition){

        return parkingFreeService.getParkingFree(queryCondition);
    }
}
