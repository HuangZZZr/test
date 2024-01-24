package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.entity.LoginInfo;
import com.rms.backend.service.LoginInfoService;
import com.rms.backend.mapper.LoginInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author 16600
* @description 针对表【login_info】的数据库操作Service实现
* @createDate 2024-01-15 11:53:54
*/
@Service
public class LoginInfoServiceImpl extends ServiceImpl<LoginInfoMapper, LoginInfo>
    implements LoginInfoService{

}




