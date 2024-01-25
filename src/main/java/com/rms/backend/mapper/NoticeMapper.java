package com.rms.backend.mapper;

import com.rms.backend.entity.Notice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 刘恒
* @description 针对表【notice】的数据库操作Mapper
* @createDate 2024-01-13 10:57:39
* @Entity com.rms.backend.entity.Notice
*/
public interface NoticeMapper extends BaseMapper<Notice> {

    Notice getnoticeData();
}




