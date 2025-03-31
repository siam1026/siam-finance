package com.siam.system.modular.mod_finance.service_impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.siam.system.modular.mod_finance.model.param.SettingParam;
import com.siam.system.modular.mod_user.auth.cache.MemberSessionManager;
import com.siam.system.modular.mod_user.entity.Member;
import com.siam.system.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.siam.system.modular.mod_finance.entity.Setting;
import com.siam.system.modular.mod_finance.mapper.SettingMapper;
import com.siam.system.modular.mod_finance.service.SettingService;

import java.util.Date;

/**
 * 基础数据设置表业务实现层
 *
 * @author JiangP
 * @date 2022/01/23 22:04
 */
@Service
public class SettingServiceImpl extends ServiceImpl<SettingMapper, Setting> implements SettingService {

    @Autowired
    private SettingMapper settingMapper;

    @Autowired
    private MemberSessionManager memberSessionManager;

    @Override
    public Setting selectCurrent() {
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.last(" limit 1");
        queryWrapper.eq("member_id", loginMember.getId());
        return settingMapper.selectOne(queryWrapper);
    }

    @Override
    public void update(SettingParam param) {
        param.setUpdateTime(new Date());
        settingMapper.updateById(param);
    }
}