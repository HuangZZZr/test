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
import com.rms.backend.form.ParkingFeeForm;
import com.rms.backend.mapper.DrivewayMapper;
import com.rms.backend.mapper.OwnerDriMapper;
import com.rms.backend.mapper.OwnerMapper;
import com.rms.backend.service.ParkingFreeService;
import com.rms.backend.mapper.ParkingFreeMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
    @Resource
    private OwnerMapper ownerMapper;

    @Override
    public ResponseResult parkingFeeList(QueryCondition<Driveway> queryCondition) {
        Page<ParkingFree> parkingFreePage = new Page<>(queryCondition.getPage(), queryCondition.getLimit());
//        先判断是否传nos过来，根据车位号获取did
        Integer did = null;
        if (ObjectUtils.isNotEmpty(queryCondition.getQuery().getNos())) {
            Driveway driveway = drivewayMapper.selectOne(new QueryWrapper<Driveway>().lambda().eq(Driveway::getNos, queryCondition.getQuery().getNos()));
            if (ObjectUtils.isEmpty(driveway)) {
                return ResponseResult.fail().message("无该车位");
            }
            did = driveway.getId();
        }
        LambdaQueryWrapper<ParkingFree> lambda = new QueryWrapper<ParkingFree>().lambda();
        lambda.eq(ObjectUtils.isNotEmpty(did), ParkingFree::getDid, did).orderByDesc(ParkingFree::getUpdateTime);
        baseMapper.selectPage(parkingFreePage, lambda);

        long total = parkingFreePage.getTotal();
        List<ParkingFree> parkingFrees = parkingFreePage.getRecords();
        List<ParkingFeeForm> collect = parkingFrees.stream().map(parkingFree -> {
            //获取时间，判断是否达30天，到达则扣费更新
            Date updateTime = parkingFree.getUpdateTime();
            Integer days = dayGet(updateTime);
            if (days==30){
                parkingFree.setPayMoney(0).setBalance(parkingFree.getBalance()-120).setUpdateTime(new Date());
                baseMapper.updateById(parkingFree);
            }

            //创建新对象封装
            ParkingFeeForm parkingFeeForm = new ParkingFeeForm();
            BeanUtils.copyProperties(parkingFree, parkingFeeForm);
            //获取业主账户
            String username = ownerMapper.selectById(parkingFree.getOid()).getUsername();
            parkingFeeForm.setAccount(username);
            //获取车位号
            String nos = drivewayMapper.selectById(parkingFree.getDid()).getNos();
            parkingFeeForm.setNos(nos);
            return parkingFeeForm;
        }).collect(Collectors.toList());
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageData", collect);
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
            ownerDriMapper.delete(new QueryWrapper<OwnerDri>().lambda().eq(OwnerDri::getDid, driveway.getId()));
        }
        baseMapper.deleteBatchIds(ids);
        return ResponseResult.success();
    }

    //    时间差计算，传入过去时间
    public Integer dayGet(Date oldTime) {
        Long starTime = Date.parse(String.valueOf(oldTime));//开始时间
        Long endTime = Date.parse(String.valueOf(new Date()));//当前时间
        Long num = endTime - starTime;//时间戳相差的毫秒数
        System.out.println("相差天数为：" + num / 24 / 60 / 60 / 1000);//除以一天的毫秒数
        Integer days = (int) (num / 24 / 60 / 60 / 1000);
        return days;
    }
}




