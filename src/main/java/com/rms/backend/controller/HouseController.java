package com.rms.backend.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.House;
import com.rms.backend.service.HouseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Projectname: rms-backend
 * @Filename: HouseController
 * @Author: LH
 * @Data:2024/1/10 20:31
 */
@RestController
@RequestMapping("house")
@Slf4j
public class HouseController {


    @Resource
    private HouseService houseService;


    //房屋列表
    @PostMapping("/list")
    public ResponseResult houseList(@RequestBody QueryCondition<House> queryCondition){
        return houseService.houseList(queryCondition);
    }

    //查看信息
    @PostMapping("view")
    public ResponseResult viewHouse(@RequestBody Integer id){
        return houseService.veiwHouse(id);
    }


    //房屋修改所属人


    //批量导入
    @PostMapping("houseImport")
    public ResponseResult houseImport(MultipartFile file){
        return houseService.houseImport(file);
    }




    //批量导出
    @PostMapping("houseExport")
    public void houseExport(HttpServletResponse response,Integer uid,String numbering,Integer statue) throws IOException {


        LambdaQueryWrapper<House> lambda = new QueryWrapper<House>().lambda();
        lambda.eq(ObjectUtils.isNotEmpty(uid),House::getUid,uid)
                .eq(StringUtils.isNotEmpty(numbering),House::getNumbering,numbering)
                .eq(ObjectUtils.isNotEmpty(statue),House::getStatue,statue);

        List<House> list = houseService.list(lambda);

        ExportParams exportParams = new ExportParams("房屋信息", "房屋表");

        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, House.class, list);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }
}
