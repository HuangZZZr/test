package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.entity.LogInfo;
import com.rms.backend.service.LogInfoService;
import com.rms.backend.mapper.LogInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author 16600
* @description 针对表【log_info】的数据库操作Service实现
* @createDate 2024-01-15 11:32:47
*/
@Service
public class LogInfoServiceImpl extends ServiceImpl<LogInfoMapper, LogInfo>
    implements LogInfoService{

}




