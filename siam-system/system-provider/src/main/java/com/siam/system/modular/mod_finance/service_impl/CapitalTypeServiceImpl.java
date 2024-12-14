package com.siam.system.modular.mod_finance.service_impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.siam.system.modular.mod_finance.entity.CapitalType;
import com.siam.system.modular.mod_finance.mapper.CapitalTypeMapper;
import com.siam.system.modular.mod_finance.service.CapitalTypeService;
import com.siam.system.modular.mod_user.auth.cache.MemberSessionManager;
import com.siam.system.modular.mod_user.entity.Member;
import com.siam.system.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CapitalTypeServiceImpl extends ServiceImpl<CapitalTypeMapper, CapitalType> implements CapitalTypeService {

    @Autowired
    private CapitalTypeMapper capitalTypeMapper;

    @Autowired
    private MemberSessionManager memberSessionManager;

    public void deleteByPrimaryKey(Integer id){
        capitalTypeMapper.deleteById(id);
    }

    public void insertSelective(CapitalType record){
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        record.setMemberId(loginMember.getId());
        capitalTypeMapper.insert(record);
    }

    public CapitalType selectByPrimaryKey(Integer id){
        return capitalTypeMapper.selectById(id);
    }

    public void updateByPrimaryKeySelective(CapitalType record){
        capitalTypeMapper.updateById(record);
    }

    @Override
    public Page getListByPage(int pageNo, int pageSize, CapitalType capitalType) {
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        capitalType.setMemberId(loginMember.getId());
        Page<Map<String, Object>> page = capitalTypeMapper.getListByPage(new Page(pageNo, pageSize), capitalType);
        return page;
    }

    @Override
    public CapitalType selectByName(String name) {
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        return capitalTypeMapper.selectByName(name, loginMember.getId());
    }
}