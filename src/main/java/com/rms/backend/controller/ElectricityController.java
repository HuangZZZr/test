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
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
    @RequiresPermissions("rms:ele:sel")
    public ResponseResult eleList(@RequestBody QueryCondition<House> queryCondition){
        return electricityService.eleList(queryCondition);

    }


    //批量导出
    @GetMapping("eleExport")
    @RequiresPermissions("rms:ele:export")
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

//    //修改电费
//    @PutMapping("eleUpData")
//    @RequiresPermissions("rms:ele:update")
//    @Logs(model = "电费",operation = Operation.UPDATE)
//    public ResponseResult eleUpData(@RequestBody Electricity electricity){
//        Double amount = electricity.getAmount();
//        Double balance = electricity.getBalance();
//        electricity.setBalance(amount+balance);
//        electricityService.saveOrUpdate(electricity);
//        return ResponseResult.success();
//
//    }

    //电费结算
    @PutMapping("eleUpData")
    @Logs(model = "电费",operation = Operation.UPDATE)
    public ResponseResult waterUpData(@RequestBody Electricity electricity){
        //计算余额
        Double ebefore = electricity.getEbefore();
        Double enow = electricity.getEnow();
        Double data=enow-ebefore;
        double w=0;
        if (data>100){
            w=(data-100)*4+50*3+50*2;
        } else if (data>50&&data<100) {
            w=(data-50)*3+50*2;
        }else {
            w=data*2;
        }
        Double amount = electricity.getAmount()-w;
        if (amount<0){
            return ResponseResult.success().message("电费余额不足，请及时缴纳");
        }else {
            electricity.setAmount(amount);
            electricity.setPayment(0.0);
            electricity.setEbefore(electricity.getEnow());
            electricity.setEnow(0.0);
            electricityService.updateById(electricity);
            return ResponseResult.success();}
    }

    //电费充值
    @PutMapping("eleRecharge")
    @Logs(model = "电费充值",operation = Operation.UPDATE)
    public ResponseResult eleUpData(@RequestBody Electricity electricity){
        Double amount = electricity.getAmount();
        Double balance = electricity.getPayment();
        electricity.setAmount(amount+balance);
        electricityService.saveOrUpdate(electricity);
        return ResponseResult.success().message("充值成功");

    }

    @PostMapping("eleNowAdd")
    public ResponseResult waterNowAdd(@RequestBody Electricity electricity){
        Electricity one = electricityService.getById(electricity.getId());
        if (one.getEbefore() > electricity.getEnow()){
            return ResponseResult.fail().message("抄表数值有误");
        }
        electricityService.updateById(electricity);
        return ResponseResult.success();
    }

    //更具ID查看
    @GetMapping ("eleID/{id}")
    public ResponseResult eleID(@PathVariable Integer id){
        Electricity electricitybyId = electricityService.getById(id);

        return ResponseResult.success().data(electricitybyId);
    }
}

