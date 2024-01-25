package com.rms.backend.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Electricity;
import com.rms.backend.entity.House;
import com.rms.backend.entity.Water;
import com.rms.backend.service.ElectricityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Projectname: rms-backend
 * @Filename: ElectricityController
 * @Author: LH
 * @Data:2024/1/10 20:32
 */
@RestController
@RequestMapping("ele")
@Slf4j
public class ElectricityController {

    @Resource
    private ElectricityService electricityService;

    //分页查询
    @PostMapping("list")
    public ResponseResult eleList(@RequestBody QueryCondition<House> queryCondition){
        return electricityService.eleList(queryCondition);

    }


    //批量导出
    @PostMapping("eleExport")
    public void waterExport(HttpServletResponse response) throws IOException {

        List<Electricity> list = electricityService.list();

        ExportParams exportParams = new ExportParams("房屋电费信息", "电费表");

        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, Electricity.class, list);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }

    //修改电费
    @PostMapping("eleUpData")
    private ResponseResult eleUpData(Electricity electricity){
        electricityService.saveOrUpdate(electricity);
        return ResponseResult.success();

    }
}

