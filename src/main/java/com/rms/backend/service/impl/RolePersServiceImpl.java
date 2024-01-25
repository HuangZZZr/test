package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.common.ResponseResult;
import com.rms.backend.entity.OwnerRole;
import com.rms.backend.entity.RolePers;
import com.rms.backend.service.RolePersService;
import com.rms.backend.mapper.RolePersMapper;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
* @author h'p
* @description 针对表【role_pers】的数据库操作Service实现
* @createDate 2024-01-19 15:00:40
*/
@Service
public class RolePersServiceImpl extends ServiceImpl<RolePersMapper, RolePers>
    implements RolePersService{

    @Override
    public ResponseResult addPermissions(Map<String,Object> rolePers) {
          //首先先全部删除
        Integer rid = (Integer) rolePers.get("roleId");
        List<Integer> ids = (List<Integer>) rolePers.get("ids");
        //通过id删除
        LambdaQueryWrapper<RolePers> queryWrapper = new QueryWrapper<RolePers>().lambda();
        queryWrapper.eq(RolePers::getRid,rid);
        baseMapper.delete(queryWrapper);
        //循环新增
        ids.forEach(id->{
            RolePers rolePer= new RolePers();
            rolePer.setPid(id);
            rolePer.setRid(rid);
            baseMapper.insert(rolePer);
        });
       return ResponseResult.success().message("新增权限成功");
    }
}




