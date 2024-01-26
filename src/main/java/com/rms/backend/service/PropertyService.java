package com.rms.backend.service;

import com.rms.backend.common.QueryCondition;
import com.rms.backend.common.ResponseResult;
import com.rms.backend.entity.Property;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rms.backend.form.LoginForm;
import com.rms.backend.form.PasswordForm;
import com.rms.backend.form.ProPertyForm;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author h'p
* @description 针对表【property】的数据库操作Service
* @createDate 2024-01-08 17:25:09
*/
public interface PropertyService extends IService<Property> {

    ResponseResult login(LoginForm loginForm,HttpServletRequest request);

    ResponseResult batchDelete(List<Integer> asList);

    ResponseResult getList(QueryCondition<Property> queryCondition);


    ResponseResult editInfo(ProPertyForm proPertyForm, HttpServletRequest request);

    ResponseResult editPassword(PasswordForm passwordForm, HttpServletRequest request);
}
