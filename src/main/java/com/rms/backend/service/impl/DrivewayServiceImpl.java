package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Driveway;
import com.rms.backend.entity.OwnerDri;
import com.rms.backend.entity.ParkingFree;
import com.rms.backend.mapper.OwnerDriMapper;
import com.rms.backend.mapper.ParkingFreeMapper;
import com.rms.backend.service.DrivewayService;
import com.rms.backend.mapper.DrivewayMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author YiXin
 * @description 针对表【driveway】的数据库操作Service实现
 * @createDate 2024-01-23 13:49:29
 */
@Service
@Transactional
public class DrivewayServiceImpl extends ServiceImpl<DrivewayMapper, Driveway>
        implements DrivewayService {
    @Resource
    private ParkingFreeMapper parkingFreeMapper;
    @Resource
    private OwnerDriMapper ownerDriMapper;
    @Override
    public ResponseResult selList(QueryCondition<Driveway> queryCondition) {
        Page<Driveway> drivewayPage = new Page<>(queryCondition.getPage(), queryCondition.getLimit());
        LambdaQueryWrapper<Driveway> lambda = new QueryWrapper<Driveway>().lambda();
        lambda.eq(ObjectUtils.isNotEmpty(queryCondition.getQuery().getId()), Driveway::getId, queryCondition.getQuery().getId())
                .eq(StringUtils.isNotEmpty(queryCondition.getQuery().getCarKind()), Driveway::getCarKind, queryCondition.getQuery().getCarKind())
                .eq(ObjectUtils.isNotEmpty(queryCondition.getQuery().getStatue()), Driveway::getStatue, queryCondition.getQuery().getStatue())
                .orderByAsc(Driveway::getId);

        baseMapper.selectPage(drivewayPage, lambda);
        List<Driveway> drivewayList = drivewayPage.getRecords();
        long total = drivewayPage.getTotal();
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageData", drivewayList);
        map.put("total", total);

        return ResponseResult.success().data(map);
    }

    @Override
    public ResponseResult delBatchDriveway(Integer[] ids) {
        List<Integer> newIds = Arrays.asList(ids);
        for (Integer newId : newIds) {
            ParkingFree parkingFree = parkingFreeMapper.selectOne(new QueryWrapper<ParkingFree>().lambda().eq(ParkingFree::getDid, newId));
            if (ObjectUtils.isNotEmpty(parkingFree)) {
                //      判断车位余额是否欠费
                if (parkingFree.getBalance() < 0) {
                    return ResponseResult.fail().message(parkingFree.getDid() + "车位租用尚且欠费，请结清余额");
                } else {
                    parkingFreeMapper.deleteById(parkingFree);
                    //      删除关联表和车位业主关联的信息
                    ownerDriMapper.delete(new QueryWrapper<OwnerDri>().lambda().eq(OwnerDri::getDid,parkingFree.getDid()));
                }
            }
            baseMapper.deleteById(newId);
        }
        return ResponseResult.success();
    }

    @Override
    public ResponseResult saveOrUpdateDavewat(Driveway driveway) {
        if (ObjectUtils.isEmpty(driveway.getId())){
            baseMapper.insert(driveway);
        }else if (driveway.getStatue()==0){
            //      改变车位状态为未用，需对停车费表进行删除操作
            ParkingFree parkingFree = parkingFreeMapper.selectOne(new QueryWrapper<ParkingFree>().lambda().eq(ParkingFree::getDid, driveway.getId()));
            if (ObjectUtils.isNotEmpty(parkingFree)) {
                //      判断车位余额是否欠费
                if (parkingFree.getBalance() < 0) {
                    return ResponseResult.fail().message(parkingFree.getDid() + "车位租用尚且欠费，请结清余额");
                } else {
                    parkingFreeMapper.deleteById(parkingFree);
                }
            }
            baseMapper.updateById(driveway);
        }else {
            baseMapper.updateById(driveway);
        }
        return ResponseResult.success();
    }
}




