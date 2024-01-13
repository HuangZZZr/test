package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.entity.Notice;
import com.rms.backend.service.NoticeService;
import com.rms.backend.mapper.NoticeMapper;
import org.springframework.stereotype.Service;

/**
* @author 刘恒
* @description 针对表【notice】的数据库操作Service实现
* @createDate 2024-01-13 10:57:39
*/
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice>
    implements NoticeService{

}




