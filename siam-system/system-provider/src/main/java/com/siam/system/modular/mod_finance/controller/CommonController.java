package com.siam.system.modular.mod_finance.controller;

import com.alibaba.fastjson.JSON;
import com.siam.package_common.constant.BusinessType;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.service.EmailService;
import com.siam.package_common.util.RedisUtils;
import com.siam.system.modular.mod_finance.model.param.CommonParam;
import com.siam.system.modular.mod_finance.model.param.FundParam;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/rest")
@Transactional(rollbackFor = Exception.class)
@Api(tags = "公共模块相关接口", description = "CommonController")
public class CommonController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 发送邮件测试
     *
     * @author JiangP
     */
    @PostMapping(value = "/testEmail")
    public BasicResult testEmail(@RequestBody @Validated(value = {}) FundParam param) {
        String text = "test";
        emailService.sendMail("snowflake_jp@126.com", "test", "test", "https://paintingstone-hangzhou.oss-cn-hangzhou.aliyuncs.com/data/images/admin/0/paintingstone_1631091641018.png", "Redis - 集群搭建(Cluster).png");
        return BasicResult.success();
    }

    /**
     * 开启/关闭数据加密功能
     *
     * @author JiangP
     */
    @GetMapping(value = "/updateIsEncrypt")
    public BasicResult updateIsEncrypt(CommonParam param) {
        redisUtils.set("isEncrypt", JSON.toJSONString(param.getIsEncrypt()));
        return BasicResult.success();
    }
}