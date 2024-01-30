package com.rms.backend.controller;

import com.rms.backend.commons.Logs;
import com.rms.backend.commons.Operation;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Notice;
import com.rms.backend.mapper.NoticeMapper;
import com.rms.backend.service.NoticeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Projectname: rms-backend
 * @Filename: NoticeController
 * @Author: LH
 * @Data:2024/1/13 11:30
 */

@RestController
@RequestMapping("notice")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    @Resource
    private NoticeMapper noticeMapper;

    //分页查询
    @PutMapping("noticeList")
    @RequiresPermissions("rms:notice:sel")
    public ResponseResult noticeList(@RequestBody QueryCondition<Notice> queryCondition){
        return noticeService.noticeList(queryCondition);

    }


    //新增
    @PostMapping("noticeAdd")
    @RequiresPermissions("rms:notice:add")
    @Logs(model = "公告",operation = Operation.ADD)
    public ResponseResult noticeAdd(@RequestBody Notice notice){
        noticeService.save(notice);
        return ResponseResult.success();
    }

    //删除
    @DeleteMapping("{id}")
    @RequiresPermissions("rms:notice:delete")
    @Logs(model = "公告",operation = Operation.DELETE)
    public ResponseResult noticeDle(@PathVariable Integer id){

        noticeMapper.deleteById(id);
        return ResponseResult.success();

    }



}
