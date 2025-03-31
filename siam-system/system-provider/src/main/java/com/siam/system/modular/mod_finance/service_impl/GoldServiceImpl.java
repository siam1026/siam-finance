package com.siam.system.modular.mod_finance.service_impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.system.modular.mod_finance.api.GoldApiService;
import com.siam.system.modular.mod_finance.vo.TTFund;
import com.siam.system.modular.mod_finance.entity.Gold;
import com.siam.system.modular.mod_finance.model.example.GoldExample;
import com.siam.system.modular.mod_finance.mapper.GoldMapper;
import com.siam.system.modular.mod_finance.service.GoldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class GoldServiceImpl implements GoldService {

    @Autowired
    private GoldMapper goldMapper;

    @Autowired
    private GoldApiService goldApiService;

    public int countByExample(GoldExample example){
        return goldMapper.countByExample(example);
    }

    @Override
    public void deleteByPrimaryKey(Integer id) {
        goldMapper.deleteByPrimaryKey(id);
    }

    public void insertSelective(Gold record){
        goldMapper.insertSelective(record);
    }

    public List<Gold> selectByExample(GoldExample example){
        return goldMapper.selectByExample(example);
    }

    public Gold selectByPrimaryKey(Integer id){
        return goldMapper.selectByPrimaryKey(id);
    }

    public void updateByExampleSelective(Gold record, GoldExample example){
        goldMapper.updateByExampleSelective(record, example);
    }

    public void updateByPrimaryKeySelective(Gold record){
        goldMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Page<Gold> getListByPage(int pageNo, int pageSize, Gold gold) {
        Page<Gold> page = goldMapper.getListByPage(new Page(pageNo, pageSize), gold);
        return page;
    }

    @Override
    public void syncPrice() {
        List<Gold> goldList = this.selectByExample(new GoldExample());
        goldList.forEach(gold -> {
            //博时黄金 -- 博时黄金ETF联接C(002611)
            //易方达黄金 -- 易方达黄金ETF联接C(002963)
            //华安黄金 -- 华安易富黄金ETF联接C(000217)
            String fundCode = "";
            if(gold.getName().equals("博时黄金")){
                fundCode = "002611";
            }else if(gold.getName().equals("易方达黄金")){
                fundCode = "002963";
            }else if(gold.getName().equals("华安黄金")){
                fundCode = "000217";
            }

            TTFund ttFund = goldApiService.getSingleInfo(fundCode);
            if (ttFund == null){
                log.debug("\n\n》》》 syncPrice - " + fundCode + " " + gold.getName() + " is not found!");
                return;
            }
            //修改基金信息
            Gold updateGold = new Gold();
            updateGold.setId(gold.getId());
            updateGold.setLastestPrice(ttFund.getGsz());
            updateGold.setLastestPriceDate(ttFund.getGztimeDate());
            updateGold.setUpdateTime(new Date());
            this.updateByPrimaryKeySelective(updateGold);
        });
    }
}