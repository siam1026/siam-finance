package com.siam.system.modular.mod_finance.service_impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.system.modular.mod_finance.entity.CapitalType;
import com.siam.system.modular.mod_finance.model.example.CapitalTypeExample;
import com.siam.system.modular.mod_finance.mapper.CapitalTypeMapper;
import com.siam.system.modular.mod_finance.service.CapitalTypeService;
import com.siam.system.modular.mod_user.auth.cache.MemberSessionManager;
import com.siam.system.modular.mod_user.entity.Member;
import com.siam.system.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CapitalTypeServiceImpl implements CapitalTypeService {

    @Autowired
    private CapitalTypeMapper capitalTypeMapper;

    @Autowired
    private MemberSessionManager memberSessionManager;

    public int countByExample(CapitalTypeExample example){
        return capitalTypeMapper.countByExample(example);
    }

    public void deleteByPrimaryKey(Integer id){
        capitalTypeMapper.deleteByPrimaryKey(id);
    }

    public void insertSelective(CapitalType record){
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        record.setMemberId(loginMember.getId());
        capitalTypeMapper.insertSelective(record);
    }

    public List<CapitalType> selectByExample(CapitalTypeExample example){
        return capitalTypeMapper.selectByExample(example);
    }

    public CapitalType selectByPrimaryKey(Integer id){
        return capitalTypeMapper.selectByPrimaryKey(id);
    }

    public void updateByExampleSelective(CapitalType record, CapitalTypeExample example){
        capitalTypeMapper.updateByExampleSelective(record, example);
    }

    public void updateByPrimaryKeySelective(CapitalType record){
        capitalTypeMapper.updateByPrimaryKeySelective(record);
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