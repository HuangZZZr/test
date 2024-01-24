package com.rms.backend.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Driveway;
import com.rms.backend.entity.OwnerDri;
import com.rms.backend.entity.ParkingFree;
import com.rms.backend.service.DrivewayService;
import com.rms.backend.service.OwnerDriService;
import com.rms.backend.service.ParkingFreeService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("parkingFee")
public class ParkingFeeController {
    @Resource
    private ParkingFreeService parkingFreeService;

    @Resource
    private DrivewayService drivewayService;

    @Resource
    private OwnerDriService ownerDriService;
//    分页查询数据
    @PutMapping("parkingList")
    public ResponseResult parkingFeeList(@RequestBody QueryCondition<ParkingFree> queryCondition){
        return parkingFreeService.parkingFeeList(queryCondition);
    }

//    根据id获取停车信息
    @GetMapping("{pid}")
    public ResponseResult getParkingFeeById(@PathVariable Integer pid){
        ParkingFree parkingFree = parkingFreeService.getById(pid);
        return ResponseResult.success().data(parkingFree);
    }

//    批量删除
    @DeleteMapping
    public ResponseResult delParkingFees(@RequestBody Integer[] pIds){
        return parkingFreeService.delParkingFees(pIds);
    }

//    添加或修改停车费表
    @PostMapping("sOrU")
    public ResponseResult saveOrUpdate(@RequestBody ParkingFree parkingFree){
        if (ObjectUtils.isEmpty(parkingFree.getId())){
            //      如果是添加操作，对车位表车位状态进行修改1(已被使用)
            Driveway driveway = drivewayService.getById(parkingFree.getDid());
            driveway.setStatue(1);
            drivewayService.updateById(driveway);
            //      根据车位id和业主id给关联表添加
            OwnerDri ownerDri = new OwnerDri();
            ownerDri.setDid(driveway.getId());
            ownerDri.setOid(parkingFree.getOid());
            ownerDriService.save(ownerDri);
        }
        parkingFreeService.saveOrUpdate(parkingFree);
        return ResponseResult.success();
    }

//    月份更替扣钱
    @PostMapping("updateAll")
    public ResponseResult updateAll(){
        List<ParkingFree> parkingFrees = parkingFreeService.list();
        List<ParkingFree> collect = parkingFrees.stream().map(parkingFree -> {
            parkingFree.setBalance(parkingFree.getBalance() - 100);
            return parkingFree;
        }).collect(Collectors.toList());
        parkingFreeService.updateBatchById(collect);
        return ResponseResult.success();
    }

//    批量导出
    @GetMapping("batchExport")
    public void batchExport(Integer oid, Integer did, HttpServletResponse response) throws IOException {
        LambdaQueryWrapper<ParkingFree> lambda = new QueryWrapper<ParkingFree>().lambda();
        lambda.eq(ObjectUtils.isNotEmpty(oid), ParkingFree::getOid, oid)
                .eq(ObjectUtils.isNotEmpty(did), ParkingFree::getDid, did)
                .orderByDesc(ParkingFree::getUpdateTime);
        List<ParkingFree> parkingFrees = parkingFreeService.list(lambda);

        ExportParams exportParams = new ExportParams("停车费记录表", "停车费展示");
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, ParkingFree.class, parkingFrees);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);

        outputStream.close();
        workbook.close();
    }
}
