package com.siam.system.modular.mod_finance.controller.member;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.constant.BaseCode;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.system.modular.mod_finance.entity.Gold;
import com.siam.system.modular.mod_finance.model.param.FundParam;
import com.siam.system.modular.mod_finance.model.param.GoldParam;
import com.siam.system.modular.mod_finance.service.GoldService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "/rest/member/gold")
@Transactional(rollbackFor = Exception.class)
@Api(tags = "黄金信息模块相关接口", description = "GoldController")
public class GoldController {

    @Autowired
    private GoldService goldService;

    /**
     * 黄金信息列表
     *
     * @author JiangP
     */
    @PostMapping(value = "/list")
    public BasicResult listGold(@RequestBody @Validated(value = {}) GoldParam param) {
        BasicData basicResult = new BasicData();

        Page page = goldService.getListByPage(param.getPageNo(), param.getPageSize(), param);

        basicResult.setData(page);
        basicResult.setSuccess(true);
        basicResult.setCode(BaseCode.SUCCESS);
        basicResult.setMessage("查询成功");
        return basicResult;
    }

    /**
     * 添加黄金信息
     *
     * @author JiangP
     */
    @PostMapping(value = "/insert")
    public BasicResult insertGold(@RequestBody @Validated(value = {}) GoldParam param) {
        BasicResult basicResult = new BasicResult();

        //添加黄金信息
        param.setCreateTime(new Date());
        param.setUpdateTime(new Date());
        goldService.insertSelective(param);

        basicResult.setSuccess(true);
        basicResult.setCode(BaseCode.SUCCESS);
        basicResult.setMessage("添加成功");
        return basicResult;
    }

    /**
     * 修改黄金信息
     *
     * @author JiangP
     */
    @PostMapping(value = "/update")
    public BasicResult updateGold(@RequestBody @Validated(value = {}) GoldParam param) {
        BasicResult basicResult = new BasicResult();

        Gold dbGold = goldService.selectByPrimaryKey(param.getId());
        if(dbGold == null){
            basicResult.setSuccess(false);
            basicResult.setCode(BaseCode.ERR);
            basicResult.setMessage("该黄金信息不存在，修改失败");
            return basicResult;
        }

        //修改黄金信息
        param.setUpdateTime(new Date());
        goldService.updateByPrimaryKeySelective(param);

        basicResult.setSuccess(true);
        basicResult.setCode(BaseCode.SUCCESS);
        basicResult.setMessage("修改成功");
        return basicResult;
    }

    /**
     * 删除黄金信息
     *
     * @author JiangP
     */
    @PostMapping(value = "/delete")
    public BasicResult deleteGold(@RequestBody @Validated(value = {}) GoldParam param) {
        BasicResult basicResult = new BasicResult();

        Gold dbGold = goldService.selectByPrimaryKey(param.getId());
        if(dbGold == null){
            basicResult.setSuccess(false);
            basicResult.setCode(BaseCode.ERR);
            basicResult.setMessage("该黄金信息不存在，删除失败");
            return basicResult;
        }

        //删除黄金信息
        param.setUpdateTime(new Date());
        goldService.deleteByPrimaryKey(param.getId());

        basicResult.setSuccess(true);
        basicResult.setCode(BaseCode.SUCCESS);
        basicResult.setMessage("修改成功");
        return basicResult;
    }

    /**
     * 更新黄金价格
     *
     * @author JiangP
     */
    @PostMapping(value = "/syncPrice")
    public BasicResult syncPrice(@RequestBody @Validated(value = {}) FundParam param) {
        goldService.syncPrice();
        return BasicResult.success();
    }
}