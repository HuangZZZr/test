package com.rms.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rms.backend.commons.Logs;
import com.rms.backend.commons.Operation;
import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Owner;
import com.rms.backend.entity.OwnerHouse;
import com.rms.backend.form.OwnerForm;
import com.rms.backend.service.OwnerHouseService;
import com.rms.backend.service.OwnerService;
import com.rms.backend.utils.SaltUtil;
import com.rms.backend.vo.OwnerVO;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

@RestController
@RequestMapping("owner")
public class OwnerController {

    @Resource
    private OwnerService ownerService;
    @Resource
    private OwnerHouseService ownerHouseService;
    //1、查询业主信息
    @PostMapping("/list")
    @RequiresPermissions("rms:owner:sel")
    public ResponseResult ownerList(@RequestBody QueryCondition<Owner> queryCondition){

        return ownerService.ownerList(queryCondition);
    }

    //2、新增业主信息 password=666666  初始化房屋水电费

    @PostMapping("save")
    @RequiresPermissions("rms:owner:add")
    @Logs(model = "业主",operation = Operation.ADD)
    public ResponseResult addOwner(@RequestBody OwnerVO ownerVO){
        return ownerService.saveOwner(ownerVO);
    }
    //3、修改业主信息
    @PutMapping("update")
    @RequiresPermissions("rms:owner:state")
    @Logs(model = "业主",operation = Operation.UPDATE)
    public ResponseResult editOwner(@RequestBody OwnerVO ownerVO){
        Owner owner = new Owner();
        BeanUtils.copyProperties(ownerVO,owner);
        ownerService.updateById(owner);
        return ResponseResult.success().message("修改成功");
    }
    //4、删除业主信息 结算水电费 删除关联房屋水电费表、车位费表的信息

    @DeleteMapping("{id}")
    @RequiresPermissions("rms:owner:delete")
    @Logs(model = "业主",operation = Operation.DELETE)
    public ResponseResult delete(@PathVariable Integer id){
        return ownerService.removeById(id);
    }

    //重置密码
    @PutMapping("resetPwd/{oid}")
    public ResponseResult resetPwd(@PathVariable Integer oid){
        Owner owner = new Owner();
        owner.setId(oid);

        String salt = SaltUtil.createSalt(4);
        owner.setSalt(salt);

        String hex = new Md5Hash("000000", salt, 1024).toHex();
        owner.setPassword(hex);

        ownerService.updateById(owner);
        return ResponseResult.success().message("密码重置成功");
    }

    //编辑用户状态
    @PutMapping("state")
    @RequiresPermissions("rms:owner:state")
    public ResponseResult editState(@RequestBody Owner owner) {
        ownerService.updateById(owner);
        return ResponseResult.success().message("状态更新成功");
    }

    //根据id查询
    @GetMapping("/{id}")
    public ResponseResult getOwnerById(@PathVariable Integer id){
        Owner owner = ownerService.getById(id);
        OwnerVO ownerVO = new OwnerVO();
        BeanUtils.copyProperties(owner,ownerVO);
        Integer hid = ownerHouseService.getOne(new QueryWrapper<OwnerHouse>().lambda().eq(OwnerHouse::getOid, id)).getHid();
        ownerVO.setHid(hid);
        return ResponseResult.success().data(ownerVO);
    }
    //根据username查询
    @GetMapping("/getByUsername/{username}")
    public ResponseResult getByUsername(@PathVariable String username){
        Owner owner = ownerService.getOne(new QueryWrapper<Owner>().lambda().eq(Owner::getUsername,username));
        return ResponseResult.success().data(owner);
    }
}
