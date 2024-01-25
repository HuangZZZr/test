package com.rms.backend.service;

import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.House;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 刘恒
* @description 针对表【house】的数据库操作Service
* @createDate 2024-01-10 17:13:54
*/
public interface HouseService extends IService<House> {

    ResponseResult gethouseData();

    ResponseResult houseList(QueryCondition<House> queryCondition);

    ResponseResult veiwHouse(Integer id);


    ResponseResult houseImport(MultipartFile file);
}
