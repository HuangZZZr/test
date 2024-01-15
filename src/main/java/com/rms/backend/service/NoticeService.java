package com.rms.backend.service;

import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Notice;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 刘恒
* @description 针对表【notice】的数据库操作Service
* @createDate 2024-01-13 10:57:39
*/
public interface NoticeService extends IService<Notice> {

    ResponseResult noticeList(QueryCondition<Notice> queryCondition);

    ResponseResult getnoticeData();
}
