package com.rms.backend.controller;

import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Notice;
import com.rms.backend.service.NoticeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("notice")
public class NoticeController {
    @Resource
    private NoticeService noticeService;


    @PostMapping("/list")
    public ResponseResult getNoticeList(@RequestBody QueryCondition<Notice> queryCondition){
        return noticeService.getNoticeList(queryCondition);
    }
}
