package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Repair;
import com.rms.backend.service.RepairService;
import com.rms.backend.mapper.RepairMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 刘恒
* @description 针对表【repair】的数据库操作Service实现
* @createDate 2024-01-10 17:13:54
*/
@Service
public class RepairServiceImpl extends ServiceImpl<RepairMapper, Repair>
    implements RepairService{

    @Override
    public ResponseResult getpepairData() {

        List<Repair> datas=baseMapper.pepairData();
        return ResponseResult.success().data(datas);
    }
}




