package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Notice;
import com.rms.backend.service.NoticeService;
import com.rms.backend.mapper.NoticeMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 刘恒
* @description 针对表【notice】的数据库操作Service实现
* @createDate 2024-01-13 10:57:39
*/
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice>
    implements NoticeService{

    @Override
    public ResponseResult noticeList(QueryCondition<Notice> queryCondition) {
        Page<Notice> noticePage = new Page<>(queryCondition.getPage(), queryCondition.getLimit());
        LambdaQueryWrapper<Notice> lambda = new QueryWrapper<Notice>().lambda();
        lambda.like(StringUtils.isNotEmpty(queryCondition.getQuery().getTitle()),Notice::getTitle,queryCondition.getQuery().getTitle());
        baseMapper.selectPage(noticePage,lambda);
        List<Notice> records = noticePage.getRecords();

        HashMap<String, Object> noticeHashMap = new HashMap<>();
        noticeHashMap.put("pageData",records);
        noticeHashMap.put("total",noticePage.getTotal());
        return ResponseResult.success().data(noticeHashMap);
    }

    @Override
    public ResponseResult getnoticeData() {

        baseMapper.getnoticeData();
        return null;
    }
}




