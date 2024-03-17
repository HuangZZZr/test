package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.entity.Orders;
import com.rms.backend.service.OrdersService;
import com.rms.backend.mapper.OrdersMapper;
import org.springframework.stereotype.Service;

/**
* @author h'p
* @description 针对表【orders】的数据库操作Service实现
* @createDate 2024-03-16 22:01:06
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{

}




