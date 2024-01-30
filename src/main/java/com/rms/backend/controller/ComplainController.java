package com.rms.backend.controller;

import com.rms.backend.commons.Logs;
import com.rms.backend.commons.Operation;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Complain;
import com.rms.backend.service.ComplainService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;

@RestController
@RequestMapping("complain")
public class ComplainController {

    @Resource
    private ComplainService complainService;

    @PostMapping("list")
    @RequiresPermissions("rms:complain:sel")
    public ResponseResult getComplainList(@RequestBody QueryCondition<Complain> queryCondition){

        return complainService.getComplainList(queryCondition);
    }

    //添加投诉
    @PutMapping("addComplain")
    @RequiresPermissions("rms:complain:add")
    @Logs(model = "投诉",operation = Operation.ADD)
    public ResponseResult addComplain(@RequestBody Complain complain){

        complain.setComplainTime(new Date());
        complain.setStatue(0);
        complainService.save(complain);
        return ResponseResult.success().message("添加成功");
    }

    //批量投诉删除
    @DeleteMapping
    @RequiresPermissions("rms:complain:delete")
    @Logs(model = "投诉",operation = Operation.DELETE)
    public ResponseResult deleteComplain(@RequestBody Integer[] ids){
        complainService.removeBatchByIds(Arrays.asList(ids));
        return ResponseResult.success().message("删除成功");
    }

    //修改投诉状态
    @PostMapping("state")
    @RequiresPermissions("rms:complain:update")
    public ResponseResult editState (@RequestBody Complain complain){
        complainService.updateById(complain);
        return ResponseResult.success().message("状态更新成功");
        }
    //根据id查询
    @GetMapping("/{id}")
    public ResponseResult getComplainById(@PathVariable Integer id){
        Complain complain = complainService.getById(id);
        return ResponseResult.success().data(complain);
    }
}
