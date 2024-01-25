package com.rms.backend.service;

import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Owner;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rms.backend.form.OwnerForm;

import java.util.List;

/**
* @author 16600
* @description 针对表【owner】的数据库操作Service
* @createDate 2024-01-15 13:48:37
*/
public interface OwnerService extends IService<Owner> {

    ResponseResult ownerList(QueryCondition<Owner> queryCondition);

    ResponseResult saveOwner(OwnerForm ownerForm);

    ResponseResult editOwner(OwnerForm ownerForm);

    ResponseResult batchRemoveByIds(List<Integer> ids);
}
