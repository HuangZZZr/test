package com.rms.backend.controller;

import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Complain;
import com.rms.backend.service.ComplainService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

@RestController
@RequestMapping("complain")
public class ComplainController {

    @Resource
    private ComplainService complainService;

    @PostMapping("list")
    public ResponseResult getComplainList(@RequestBody QueryCondition<Complain> queryCondition){

        return complainService.getComplainList(queryCondition);
    }

    //添加投诉
    @PostMapping("addComplain")
    public ResponseResult addComplain(@RequestBody Complain complain){

        complainService.save(complain);
        return ResponseResult.success().message("添加成功");
    }

    //批量投诉删除
    @DeleteMapping
    public ResponseResult deleteComplain(@RequestBody Integer[] ids){
        complainService.removeBatchByIds(Arrays.asList(ids));
        return ResponseResult.success().message("删除成功");
    }

    //修改投诉状态
        @PutMapping("state")
    public ResponseResult editState (@RequestBody Complain complain){
        complainService.updateById(complain);
        return ResponseResult.success().message("状态更新成功");
        }
}
