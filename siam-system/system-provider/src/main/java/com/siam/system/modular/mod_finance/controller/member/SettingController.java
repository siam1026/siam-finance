package com.siam.system.modular.mod_finance.controller.member;

import com.siam.package_common.entity.BasicResult;
import com.siam.system.modular.mod_finance.entity.Setting;
import com.siam.system.modular.mod_finance.model.param.SettingParam;
import com.siam.system.modular.mod_finance.service.SettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/rest/member/setting")
@Transactional(rollbackFor = Exception.class)
@Api(tags = "后台基础数据设置模块相关接口", description = "AdminSettingController")
public class SettingController {

    @Autowired
    private SettingService settingService;

    @ApiOperation(value = "基础数据设置列表")
    @PostMapping(value = "/selectCurrent")
    public BasicResult selectCurrent(@RequestBody @Validated(value = {}) SettingParam param){
        Setting current = settingService.selectCurrent();
        return BasicResult.success(current);
    }

    @ApiOperation(value = "修改基础数据设置")
    @PutMapping(value = "/update")
    public BasicResult update(@RequestBody @Validated(value = {}) SettingParam param){
        settingService.update(param);
        return BasicResult.success();
    }
}