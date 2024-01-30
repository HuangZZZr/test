package com.rms.backend.controller;

import com.rms.backend.commons.Logs;
import com.rms.backend.commons.Operation;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Complain;
import com.rms.backend.entity.Repair;
import com.rms.backend.service.RepairService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

@RestController
@RequestMapping("repair")
public class RepairController {

    @Resource
    private RepairService repairService;

    @PutMapping("list")
    @RequiresPermissions("rms:repair:sel")
    public ResponseResult getRepairList(@RequestBody QueryCondition<Repair> queryCondition){
        return repairService.getRepairList(queryCondition);
    }


    //修改状态
    @PutMapping("state")
    @RequiresPermissions("rms:repair:update")
    public ResponseResult editState (@RequestBody Repair repair){
        repairService.updateById(repair);
        return ResponseResult.success().message("状态更新成功");
    }
    //添加维修
    @PutMapping("save")
    @RequiresPermissions("rms:repair:save")
    @Logs(model = "维修",operation = Operation.ADD)
    public ResponseResult addRepair(@RequestBody Repair repair){

        repairService.save(repair);
        return ResponseResult.success().message("添加成功");
    }

    //批量删除
    @DeleteMapping
    @RequiresPermissions("rms:repair:delete")
    @Logs(model = "维修",operation = Operation.DELETE)
    public ResponseResult deleteComplain(@RequestBody Integer[] ids){
        repairService.removeBatchByIds(Arrays.asList(ids));
        return ResponseResult.success().message("删除成功");
    }
    //根据id查询
    @GetMapping("{id}")
    public ResponseResult getRepairById(@PathVariable Integer id){
        Repair repair = repairService.getById(id);
        return ResponseResult.success().data(repair);
    }

}
