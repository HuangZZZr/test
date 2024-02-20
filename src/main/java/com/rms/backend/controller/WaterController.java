package com.rms.backend.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rms.backend.commons.Logs;
import com.rms.backend.commons.Operation;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.House;
import com.rms.backend.entity.Water;
import com.rms.backend.from.WaterFrom;
import com.rms.backend.mapper.WaterMapper;
import com.rms.backend.service.HouseService;
import com.rms.backend.service.OwnerService;
import com.rms.backend.service.WaterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Projectname: rms-backend
 * @Filename: WaterController
 * @Author: LH
 * @Data:2024/1/10 20:32
 */

@RestController
@RequestMapping("water")
@Slf4j
public class WaterController {

    @Resource
    private WaterService waterService;

    @Resource
    private OwnerService ownerService;

    @Resource
    private HouseService houseService;
    //分页查询
    @PostMapping("list")
    @RequiresPermissions("rms:water:sel")
    public ResponseResult waterList(@RequestBody QueryCondition<House> queryCondition) {
        return waterService.waterList(queryCondition);

    }


    //批量导出
    @GetMapping("waterExport")
    @RequiresPermissions("rms:water:export")
    public void waterExport(HttpServletResponse response) throws IOException {

        List<Water> list = waterService.list();
        List<WaterFrom> collect = list.stream().map(water -> {
            WaterFrom waterFrom = new WaterFrom();
            BeanUtils.copyProperties(water, waterFrom);
            String name = ownerService.getById(water.getOid()).getName();
            String numbering = houseService.getById(water.getHid()).getNumbering();
            waterFrom.setNumbering(numbering);
            waterFrom.setWname(name);
            return waterFrom;
        }).collect(Collectors.toList());

        ExportParams exportParams = new ExportParams("房屋水费信息", "水费表");
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, WaterFrom.class, collect);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }


//    //修改水费
//    @PutMapping("waterUpData")
//    @RequiresPermissions("rms:water:update")
//    @Logs(model = "水费",operation = Operation.UPDATE)
//    public ResponseResult waterUpData(@RequestBody Water water){
//        System.out.println("water = " + water);
//        Double amount = water.getAmount();
//        Double balance = water.getBalance();
//        water.setBalance(amount+balance);
//
//        waterService.updateById(water);
//        return ResponseResult.success();
//    }

    //水费结算
    @PutMapping("waterUpData")
    @Logs(model = "水费", operation = Operation.UPDATE)
    public ResponseResult waterUpData(@RequestBody Water water) {
        System.out.println("water #$%%$%$$% = " + water);
        //计算余额
        //计算余额
        Double wbefore = water.getWbefore();
        Double wnow = water.getWnow();
        Double data=(wnow-wbefore);
        double w=0;
        if (data>100){
            w=(data-100)*4+50*3+50*2;
        } else if (data>50&&data<100) {
            w=(data-50)*3+50*2;
        }else {
            w=data*2;
        }
        Double amount = water.getAmount()-w;

        if (amount < 0) {
            return ResponseResult.success().message("水费余额不足，请及时缴纳");
        } else {
            water.setAmount(amount);
            water.setPayment(0.0);
            water.setWbefore(water.getWnow());
            water.setWnow(0.0);
            waterService.updateById(water);
            return ResponseResult.success();
        }
    }


    //水费充值
    @PutMapping("waterRecharge")
    @Logs(model = "水费", operation = Operation.UPDATE)
    public ResponseResult waterRecharge(@RequestBody Water water) {
        Double payment = water.getPayment();
        Double amount = water.getAmount();
        water.setAmount(payment + amount);
        waterService.updateById(water);
        return ResponseResult.success().message("充值成功");
    }

    @PostMapping("waterNowAdd")
    public ResponseResult waterNowAdd(@RequestBody Water water){
        Water one = waterService.getById(water.getId());
        if (one.getWbefore() > water.getWnow()){
            return ResponseResult.fail().message("抄表数值有误");
        }
        waterService.updateById(water);
        return ResponseResult.success();
    }

    //更具id进行查询信息
    @GetMapping("waterID/{id}")
    public ResponseResult waterID(@PathVariable Integer id) {
        Water waterbyId = waterService.getById(id);
        return ResponseResult.success().data(waterbyId);
    }
}
