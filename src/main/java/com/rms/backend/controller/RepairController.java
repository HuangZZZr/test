package com.rms.backend.controller;

import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Repair;
import com.rms.backend.service.RepairService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

@RestController
@RequestMapping("repair")
public class RepairController {

    @Resource
    private RepairService repairService;

    @PostMapping("list")
    public ResponseResult getRepairList(@RequestBody QueryCondition<Repair> queryCondition){
        return repairService.getRepairList(queryCondition);
    }

    //修改状态
    @PutMapping("state")
    public ResponseResult editState (@RequestBody Repair repair){
        repairService.updateById(repair);
        return ResponseResult.success().message("状态更新成功");
    }

    //批量删除
    @DeleteMapping
    public ResponseResult deleteComplain(@RequestBody Integer[] ids){
        repairService.removeBatchByIds(Arrays.asList(ids));
        return ResponseResult.success().message("删除成功");
    }
//

}
