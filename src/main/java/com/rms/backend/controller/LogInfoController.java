package com.rms.backend.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.LogInfo;
import com.rms.backend.service.LogInfoService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("log")
public class LogInfoController {

    @Resource
    private LogInfoService logInfoService;

    @PostMapping("list")
    public ResponseResult getLogList(@RequestBody QueryCondition<LogInfo> queryCondition){

        Page<LogInfo> logInfoPage = new Page<>(queryCondition.getPage(), queryCondition.getLimit());
        LambdaQueryWrapper<LogInfo> lambda = new QueryWrapper<LogInfo>().lambda();
        lambda.like(StringUtils.isNotEmpty(queryCondition.getQuery().getName()),LogInfo::getName,queryCondition.getQuery().getName())
                .like(StringUtils.isNotEmpty(queryCondition.getQuery().getIp()),LogInfo::getIp,queryCondition.getQuery().getIp())
                .like(StringUtils.isNotEmpty(queryCondition.getQuery().getModel()),LogInfo::getModel,queryCondition.getQuery().getModel());

        logInfoService.page(logInfoPage,lambda);
        HashMap<String, Object> result = new HashMap<>();
        result.put("pageData",logInfoPage.getRecords());
        result.put("total",logInfoPage.getTotal());
        return ResponseResult.success().data(result);

    }

    //批量删除
    @DeleteMapping
    public ResponseResult deleteLog(@RequestBody Integer[] lids) {
        logInfoService.removeBatchByIds(Arrays.asList(lids));
        return ResponseResult.success().message("删除成功");
    }


    @GetMapping("excel")
    public void exportLog(HttpServletResponse response, String name, String ip, String model) throws IOException {

        LambdaQueryWrapper<LogInfo> lambda = new QueryWrapper<LogInfo>().lambda();
        lambda.like(StringUtils.isNotEmpty(name),LogInfo::getName,name)
                .like(StringUtils.isNotEmpty(ip),LogInfo::getIp,ip)
                .like(StringUtils.isNotEmpty(model),LogInfo::getModel,model)
                .orderByDesc(LogInfo::getCreateTime);

        List<LogInfo> list = logInfoService.list(lambda);
        ExportParams exportParams = new ExportParams("日志信息", "日志表");
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, LogInfo.class, list);
        ServletOutputStream outputStream = response.getOutputStream();

        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

}