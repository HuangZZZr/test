package com.rms.backend.controller;

import com.rms.backend.commons.ResponseResult;
import com.rms.backend.service.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Projectname: rms-backend
 * @Filename: visualizationController
 * @Author: LH
 * @Data:2024/1/9 18:51
 */

@RestController
@RequestMapping("dv")
public class VisualizationController {

    @Resource
    private NoticeService noticeService;

    @Resource
    private HouseService houseService;

    @Resource
    private RepairService repairService;

    @Resource
    private WaterService waterService;

    @Resource
    private ElectricityService electricityService;

    @Resource
    private DrivewayService drivewayService;

    //公告可视化
    @GetMapping("noticeNow")
    public ResponseResult noticeData(){
        return noticeService.getnoticeData();
    }


    //各单元住户统计
    @GetMapping("houseData")
    public ResponseResult houseData(){
        return houseService.gethouseData();
    }


    //维修信息（10条）
    @GetMapping("repairData")
    public ResponseResult repairData(){
        return repairService.getpepairData();
    }

    //水费小于10
    @GetMapping("wateData")
    public ResponseResult wateData(){
        return waterService.getwaterData();
    }


    //电费小于30
    @GetMapping("electricityData")
    public ResponseResult electricityData(){
        return electricityService.getelectricityData();
    }

    //车位闲置
    @GetMapping("drivewayData")
    public ResponseResult drivewayData(){
        return drivewayService.getdrivewayData();
    }
}
