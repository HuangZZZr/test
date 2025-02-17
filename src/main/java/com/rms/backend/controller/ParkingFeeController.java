package com.rms.backend.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.rms.backend.commons.Logs;
import com.rms.backend.commons.Operation;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Driveway;
import com.rms.backend.entity.ParkingFree;
import com.rms.backend.form.ParkingFeeForm;
import com.rms.backend.service.DrivewayService;
import com.rms.backend.service.OwnerDriService;
import com.rms.backend.service.OwnerService;
import com.rms.backend.service.ParkingFreeService;
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

@RestController
@RequestMapping("parkingFee")
public class ParkingFeeController {
    @Resource
    private ParkingFreeService parkingFreeService;

    @Resource
    private DrivewayService drivewayService;

    @Resource
    private OwnerDriService ownerDriService;
    @Resource
    private OwnerService ownerService;

    //    分页查询数据
    @PutMapping("parkingList")
    @RequiresPermissions("rms:parkingFee:sel")
    public ResponseResult parkingFeeList(@RequestBody QueryCondition<Driveway> queryCondition) {
        return parkingFreeService.parkingFeeList(queryCondition);
    }

    //    根据id获取停车信息
    @GetMapping("{pid}")
    @RequiresPermissions("rms:parkingFee:update")
    public ResponseResult getParkingFeeById(@PathVariable Integer pid) {
        ParkingFree parkingFree = parkingFreeService.getById(pid);
        ParkingFeeForm parkingFeeForm = new ParkingFeeForm();
        BeanUtils.copyProperties(parkingFree,parkingFeeForm);
//        获取用户账户
        String tel = ownerService.getById(parkingFree.getOid()).getTel();
        parkingFeeForm.setTel(tel);
//        获取车位号
        String nos = drivewayService.getById(parkingFree.getDid()).getNos();
        parkingFeeForm.setNos(nos);
        return ResponseResult.success().data(parkingFeeForm);
    }

    //    批量删除
    @DeleteMapping
    @RequiresPermissions("rms:parkingFee:delete")
    @Logs(model = "停车费",operation = Operation.DELETE)
    public ResponseResult delParkingFees(@RequestBody Integer[] pIds) {
        return parkingFreeService.delParkingFees(pIds);
    }

    //    修改停车费表
    @PostMapping("sOrU")
    @RequiresPermissions("rms:parkingFee:add")
    @Logs(model = "停车费",operation = Operation.ADD)
    public ResponseResult saveOrUpdate(@RequestBody ParkingFree parkingFree) {
        /*
        if (ObjectUtils.isEmpty(parkingFree.getId())) {
            //      如果是添加操作，对车位表车位状态进行修改1(已被使用)
            Driveway driveway = drivewayService.getById(parkingFree.getDid());
            driveway.setStatue(1);
            drivewayService.updateById(driveway);
            //      根据车位id和业主id给关联表添加
            OwnerDri ownerDri = new OwnerDri();
            ownerDri.setDid(driveway.getId());
            ownerDri.setOid(parkingFree.getOid());
            ownerDriService.save(ownerDri);
            parkingFree.setBalance(parkingFree.getPayMoney());
        }else {
            */
            parkingFree.setBalance(parkingFree.getPayMoney() + parkingFree.getBalance());
            parkingFree.setUpdateTime(new Date());
//        }
        parkingFreeService.saveOrUpdate(parkingFree);
        return ResponseResult.success();
    }

    //    月份更替扣钱
    @PostMapping("updateAll")
    @RequiresPermissions("rms:parkingFee:update")
    public ResponseResult updateAll() {
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
    @RequiresPermissions("rms:parkingFee:export")
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
