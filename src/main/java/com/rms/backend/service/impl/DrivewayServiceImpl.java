package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Driveway;
import com.rms.backend.service.DrivewayService;
import com.rms.backend.mapper.DrivewayMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 刘恒
* @description 针对表【driveway】的数据库操作Service实现
* @createDate 2024-01-10 17:13:54
*/
@Service
public class DrivewayServiceImpl extends ServiceImpl<DrivewayMapper, Driveway>
    implements DrivewayService{

    @Override
    public ResponseResult getdrivewayData() {

        List<Driveway>datas=baseMapper.drivewayData();

        return ResponseResult.success().data(datas);
    }
}




