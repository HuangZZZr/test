package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Notice;
import com.rms.backend.service.NoticeService;
import com.rms.backend.mapper.NoticeMapper;
import org.springframework.stereotype.Service;

/**
* @author 16600
* @description 针对表【notice】的数据库操作Service实现
* @createDate 2024-01-23 08:45:59
*/
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService{

    @Override
    public ResponseResult getNoticeList(QueryCondition<Notice> queryCondition) {
        Page<Notice> noticePage = new Page<>(queryCondition.getPage(), queryCondition.getLimit());
        LambdaQueryWrapper<Notice> lambda = new QueryWrapper<Notice>().lambda();


        return null;
    }
}




