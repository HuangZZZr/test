package com.rms.backend.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rms.backend.commons.Logs;
import com.rms.backend.commons.Operation;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Electricity;
import com.rms.backend.entity.House;
import com.rms.backend.entity.Water;
import com.rms.backend.service.ElectricityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("eleExport")
    public void waterExport(HttpServletResponse response) throws IOException {

        LambdaQueryWrapper<Electricity> lambda = new QueryWrapper<Electricity>().lambda();

        List<Electricity> list = electricityService.list(lambda);

        ExportParams exportParams = new ExportParams("房屋电费信息", "电费表");

        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, Electricity.class, list);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }

    //修改电费
    @PutMapping("eleUpData")
    @Logs(model = "电费",operation = Operation.UPDATE)
    private ResponseResult eleUpData(@RequestBody Electricity electricity){
        Double amount = electricity.getAmount();
        Double balance = electricity.getBalance();
        electricity.setBalance(amount+balance);
        electricityService.saveOrUpdate(electricity);
        return ResponseResult.success();

    }

    //更具ID查看
    @GetMapping ("eleID/{id}")
    public ResponseResult eleID(@PathVariable Integer id){
        Electricity electricitybyId = electricityService.getById(id);

        return ResponseResult.success().data(electricitybyId);
    }
}

