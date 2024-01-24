package com.rms.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Driveway;
import com.rms.backend.entity.OwnerDri;
import com.rms.backend.entity.ParkingFree;
import com.rms.backend.mapper.DrivewayMapper;
import com.rms.backend.mapper.OwnerDriMapper;
import com.rms.backend.service.ParkingFreeService;
import com.rms.backend.mapper.ParkingFreeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author YiXin
 * @description 针对表【parking_free】的数据库操作Service实现
 * @createDate 2024-01-24 10:36:04
 */
@Service
@Transactional
public class ParkingFreeServiceImpl extends ServiceImpl<ParkingFreeMapper, ParkingFree>
        implements ParkingFreeService {
    @Resource
    private DrivewayMapper drivewayMapper;
    @Resource
    private OwnerDriMapper ownerDriMapper;
    @Override
    public ResponseResult parkingFeeList(QueryCondition<ParkingFree> queryCondition) {
        Page<ParkingFree> parkingFreePage = new Page<>(queryCondition.getPage(), queryCondition.getLimit());
        LambdaQueryWrapper<ParkingFree> lambda = new QueryWrapper<ParkingFree>().lambda();
        lambda.eq(ObjectUtils.isNotEmpty(queryCondition.getQuery().getOid()), ParkingFree::getOid, queryCondition.getQuery().getOid())
                .eq(ObjectUtils.isNotEmpty(queryCondition.getQuery().getDid()), ParkingFree::getDid, queryCondition.getQuery().getDid())
                .orderByDesc(ParkingFree::getUpdateTime);
        baseMapper.selectPage(parkingFreePage, lambda);

        long total = parkingFreePage.getTotal();
        List<ParkingFree> parkingFrees = parkingFreePage.getRecords();
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageData", parkingFrees);
        map.put("total", total);
        return ResponseResult.success().data(map);
    }

    @Override
    public ResponseResult delParkingFees(Integer[] pIds) {
        List<Integer> ids = Arrays.asList(pIds);
        //      先判断该批量对象中，是否有欠费记录
        for (Integer id : ids) {
            ParkingFree parkingFree = baseMapper.selectById(id);
            if (parkingFree.getBalance() < 0) {
                return ResponseResult.fail().message(parkingFree.getDid() + "车位号车位欠费，请结清");
            }
            //      将车位号表被删除车位状态改成0(未用)
            Driveway driveway = drivewayMapper.selectById(parkingFree.getDid());
            driveway.setStatue(0);
            drivewayMapper.updateById(driveway);
            //      删除关联表和业主关联的信息
            ownerDriMapper.delete(new QueryWrapper<OwnerDri>().lambda().eq(OwnerDri::getDid,driveway.getId()));
        }
        baseMapper.deleteBatchIds(ids);
        return ResponseResult.success();
    }
}




