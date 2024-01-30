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
import com.rms.backend.mapper.WaterMapper;
import com.rms.backend.service.WaterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

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
    private WaterMapper waterMapper;


    //分页查询
    @PostMapping("list")
    @RequiresPermissions("rms:water:sel")
    public ResponseResult waterList(@RequestBody QueryCondition<House> queryCondition){
        return waterService.waterList(queryCondition);

    }


    //批量导出
    @GetMapping("waterExport")
    @RequiresPermissions("rms:water:export")
    public void waterExport(HttpServletResponse response) throws IOException {

        List<Water> list = waterService.list();

        ExportParams exportParams = new ExportParams("房屋水费信息", "水费表");
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, Water.class, list);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }


    //修改水费
    @PutMapping("waterUpData")
    @RequiresPermissions("rms:water:update")
    @Logs(model = "水费",operation = Operation.UPDATE)
    public ResponseResult waterUpData(@RequestBody Water water){
        System.out.println("water = " + water);
        Double amount = water.getAmount();
        Double balance = water.getBalance();
        water.setBalance(amount+balance);

        waterService.updateById(water);
        return ResponseResult.success();
    }


    //更具id进行查询信息
    @GetMapping("waterID/{id}")
    public ResponseResult waterID(@PathVariable Integer id){
        Water waterbyId = waterService.getById(id);
        return ResponseResult.success().data(waterbyId);
    }
}
