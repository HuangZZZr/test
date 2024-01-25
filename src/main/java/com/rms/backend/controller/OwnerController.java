package com.rms.backend.controller;

import com.rms.backend.commons.QueryCondition;
import com.rms.backend.commons.ResponseResult;
import com.rms.backend.entity.Owner;
import com.rms.backend.form.OwnerForm;
import com.rms.backend.service.OwnerService;
import com.rms.backend.utils.SaltUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

@RestController
@RequestMapping("owner")
public class OwnerController {

    @Resource
    private OwnerService ownerService;

    //1、查询业主信息
    @PostMapping("/list")
    public ResponseResult ownerList(@RequestBody QueryCondition<Owner> queryCondition){

        return ownerService.ownerList(queryCondition);
    }

    //2、新增业主信息 password=666666  初始化房屋水电费

    @PostMapping
    public ResponseResult addOwner(@RequestBody OwnerForm ownerForm){
        return ownerService.saveOwner(ownerForm);
    }
    //3、修改业主信息
    @PutMapping("editOwner")
    public ResponseResult editOwner(@RequestBody OwnerForm ownerForm){
        return ownerService.editOwner(ownerForm);
    }
    //4、删除业主信息 结算水电费 删除关联房屋水电费表、车位费表的信息

    @DeleteMapping
    public ResponseResult delete(@RequestBody Integer[] oids){
        return ownerService.batchRemoveByIds(Arrays.asList(oids));
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
    public ResponseResult editState(@RequestBody Owner owner) {
        ownerService.updateById(owner);
        return ResponseResult.success().message("状态更新成功");
    }
}
