package com.siam.system.modular.mod_finance.service_impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.system.modular.mod_finance.entity.GoldShorttermTrading;
import com.siam.system.modular.mod_finance.model.example.GoldShorttermTradingExample;
import com.siam.system.modular.mod_finance.mapper.GoldShorttermTradingMapper;
import com.siam.system.modular.mod_finance.model.result.GoldShorttermTradingResult;
import com.siam.system.modular.mod_finance.service.GoldShorttermTradingService;
import com.siam.system.modular.mod_user.auth.cache.MemberSessionManager;
import com.siam.system.modular.mod_user.entity.Member;
import com.siam.system.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GoldShorttermTradingServiceImpl implements GoldShorttermTradingService {

    @Autowired
    private GoldShorttermTradingMapper goldShorttermTradingMapper;

    @Autowired
    private MemberSessionManager memberSessionManager;

    public int countByExample(GoldShorttermTradingExample example){
        return goldShorttermTradingMapper.countByExample(example);
    }

    @Override
    public void deleteByPrimaryKey(Integer id) {
        goldShorttermTradingMapper.deleteByPrimaryKey(id);
    }

    public void insertSelective(GoldShorttermTrading record){
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        record.setMemberId(loginMember.getId());
        goldShorttermTradingMapper.insertSelective(record);
    }

    public List<GoldShorttermTrading> selectByExample(GoldShorttermTradingExample example){
        return goldShorttermTradingMapper.selectByExample(example);
    }

    public GoldShorttermTrading selectByPrimaryKey(Integer id){
        return goldShorttermTradingMapper.selectByPrimaryKey(id);
    }

    public void updateByExampleSelective(GoldShorttermTrading record, GoldShorttermTradingExample example){
        goldShorttermTradingMapper.updateByExampleSelective(record, example);
    }

    public void updateByPrimaryKeySelective(GoldShorttermTrading record){
        goldShorttermTradingMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Page<GoldShorttermTradingResult> getListByPage(int pageNo, int pageSize, GoldShorttermTrading goldShorttermTrading) {
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        goldShorttermTrading.setMemberId(loginMember.getId());
        Page<GoldShorttermTradingResult> page = goldShorttermTradingMapper.getListByPage(new Page(pageNo, pageSize), goldShorttermTrading);
        return page;
    }

    @Override
    public BigDecimal selectSumGramsByRelatedTradingId(Integer relatedTradingId) {
        return goldShorttermTradingMapper.selectSumGramsByRelatedTradingId(relatedTradingId);
    }

    @Override
    public BigDecimal selectSumAmountByRelatedTradingId(Integer relatedTradingId) {
        return goldShorttermTradingMapper.selectSumAmountByRelatedTradingId(relatedTradingId);
    }

    @Override
    public BigDecimal selectSumSellSharesByRelatedTradingId(Integer relatedTradingId) {
        return goldShorttermTradingMapper.selectSumSellSharesByRelatedTradingId(relatedTradingId);
    }
}