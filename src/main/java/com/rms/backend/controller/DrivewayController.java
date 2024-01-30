package com.rms.backend.controller;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.rms.backend.commons.Logs;
import com.rms.backend.commons.Operation;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Driveway;
import com.rms.backend.service.DrivewayService;
import com.rms.backend.service.ParkingFreeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Optional;

@RestController
@RequestMapping("driver")
public class DrivewayController {
    @Resource
    private DrivewayService drivewayService;

    // 分页查询获取车位类型表单
    @PutMapping("selList")
    @RequiresPermissions("rms:driver:sel")
    public ResponseResult selList(@RequestBody QueryCondition<Driveway> queryCondition){
        return drivewayService.selList(queryCondition);
    }

    //    根据id获取车位信息
    @GetMapping("{id}")
    @RequiresPermissions("rms:driver:update")
    public ResponseResult getOneById(@PathVariable Integer id){
        Optional<Driveway> driveway = drivewayService.getOptById(id);
        return ResponseResult.success().data(driveway);
    }

    //    添加或保存车位信息
    @PostMapping("saveOrUpdate")
    @RequiresPermissions("rms:driverway:add")
    @Logs(model = "车位",operation = Operation.ADD)
    public ResponseResult saveOrUpdate(@RequestBody Driveway driveway){
        if (ObjectUtils.isEmpty(driveway.getId())){
            //     初始车位添加强制为未使用
            driveway.setStatue(0);
        }
        return drivewayService.saveOrUpdateDavewat(driveway);
    }

    //    批量删除
    @DeleteMapping
    @RequiresPermissions("rms:driver:delete")
    @Logs(model = "车位",operation = Operation.DELETE)
    public ResponseResult delBatch(@RequestBody Integer[] ids){
        return drivewayService.delBatchDriveway(ids);
    }
}

