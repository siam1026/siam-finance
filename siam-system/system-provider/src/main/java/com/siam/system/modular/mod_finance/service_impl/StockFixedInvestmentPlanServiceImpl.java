package com.siam.system.modular.mod_finance.service_impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.siam.package_common.exception.StoneCustomerException;
import com.siam.package_common.util.OSSUtils;
import com.siam.system.modular.mod_finance.entity.*;
import com.siam.system.modular.mod_finance.mapper.StockFixedInvestmentPlanMapper;
import com.siam.system.modular.mod_finance.model.param.StockFixedInvestmentPlanParam;
import com.siam.system.modular.mod_finance.service.StockFixedInvestmentService;
import com.siam.system.modular.mod_finance.service.StockService;
import com.siam.system.modular.mod_user.auth.cache.MemberSessionManager;
import com.siam.system.modular.mod_user.entity.Member;
import com.siam.system.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.siam.system.modular.mod_finance.entity.StockFixedInvestmentPlan;
import com.siam.system.modular.mod_finance.service.StockFixedInvestmentPlanService;

import java.util.Date;
import java.util.Map;

/**
 * 股票定投计划表业务实现层
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
@Service
public class StockFixedInvestmentPlanServiceImpl extends ServiceImpl<StockFixedInvestmentPlanMapper, StockFixedInvestmentPlan> implements StockFixedInvestmentPlanService {

    @Autowired
    private StockFixedInvestmentPlanMapper stockFixedInvestmentPlanMapper;

    @Autowired
    private OSSUtils ossUtils;

    @Autowired
    private StockService stockService;

    @Autowired
    private StockFixedInvestmentService stockFixedInvestmentService;

    @Autowired
    private MemberSessionManager memberSessionManager;

    @Override
    public Page<StockFixedInvestmentPlan> getListByPage(int pageNo, int pageSize, StockFixedInvestmentPlan stockFixedInvestmentPlan) {
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        stockFixedInvestmentPlan.setMemberId(loginMember.getId());
        Page<StockFixedInvestmentPlan> page = stockFixedInvestmentPlanMapper.getListByPage(new Page(pageNo, pageSize), stockFixedInvestmentPlan);
        return page;
    }

    @Override
    public Page<Map<String, Object>> getMapListByPage(int pageNo, int pageSize, StockFixedInvestmentPlan stockFixedInvestmentPlan) {
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        stockFixedInvestmentPlan.setMemberId(loginMember.getId());
        Page<Map<String, Object>> page = stockFixedInvestmentPlanMapper.getMapListByPage(new Page(pageNo, pageSize), stockFixedInvestmentPlan);
        //获取基金名称
        page.getRecords().forEach(stringObjectMap -> {
            stringObjectMap.put("stockName", getStockNames((String) stringObjectMap.get("stockIdComma")));
        });
        return page;
    }

    /**
     * 获取基金名称
     */
    public String getStockNames(String stockIdComma){
        String result = "";
        String[] arr = stockIdComma.split(",");
        for (String stockId : arr) {
            Stock dbStock = stockService.getById(Integer.valueOf(stockId));
            if(result.equals("")){
                result = dbStock.getName();
            }else{
                result = "," + dbStock.getName();
            }
        }
        return result;
    }

    @Override
    public void insert(StockFixedInvestmentPlanParam param) {
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        param.setMemberId(loginMember.getId());
        //添加基金定投计划
        param.setCreateTime(new Date());
        param.setUpdateTime(new Date());
        stockFixedInvestmentPlanMapper.insert(param);
    }

    @Override
    public void update(StockFixedInvestmentPlanParam param) {
        StockFixedInvestmentPlan dbStockFixedInvestmentPlan = stockFixedInvestmentPlanMapper.selectById(param.getId());
        if(dbStockFixedInvestmentPlan == null){
            throw new StoneCustomerException("该基金定投计划不存在，修改失败");
        }
        //修改基金定投计划
        param.setUpdateTime(new Date());
        stockFixedInvestmentPlanMapper.updateById(param);
    }

    @Override
    public void delete(StockFixedInvestmentPlanParam param) {
        StockFixedInvestmentPlan dbStockFixedInvestmentPlan = stockFixedInvestmentPlanMapper.selectById(param.getId());
        if(dbStockFixedInvestmentPlan == null){
            throw new StoneCustomerException("该基金定投计划不存在，删除失败");
        }
        //删除基金定投计划
        param.setUpdateTime(new Date());
        stockFixedInvestmentPlanMapper.deleteById(param.getId());
    }

    @Override
    public void updateStatus(int id) {
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        StockFixedInvestmentPlan dbPlan = this.getById(id);
        if(dbPlan == null){
            return;
        }
        if(dbPlan.getStatus().equals(StockFixedInvestmentPlan.STATUS_COMPLETED)){
            return;
        }

        //查询定投交易记录总买入记录数量
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("plan_id", id);
        queryWrapper.eq("type", StockFixedInvestment.TYPE_BUY);
        queryWrapper.eq("member_id", loginMember.getId());
        Integer allCount = stockFixedInvestmentService.getBaseMapper().selectCount(queryWrapper);
        //查询定投交易记录买入记录 - 进行中的数量
        queryWrapper.eq("status", StockFixedInvestment.STATUS_IN_PROGRESS);
        queryWrapper.eq("member_id", loginMember.getId());
        Integer inProgressCount = stockFixedInvestmentService.getBaseMapper().selectCount(queryWrapper);

        //获取状态
        int status;
        if(allCount>0 && inProgressCount==0){
            status = StockFixedInvestmentPlan.STATUS_CLEARANCE;
        }else if(allCount>0 && inProgressCount>0){
            status = StockFixedInvestmentPlan.STATUS_IN_PROGRESS;
        }else{
            status = StockFixedInvestmentPlan.STATUS_NOT_STARTED;
        }

        if(!dbPlan.getStatus().equals(status)){
            //修改状态
            StockFixedInvestmentPlan updatePlan = new StockFixedInvestmentPlan();
            updatePlan.setId(id);
            updatePlan.setStatus(status);
            this.updateById(updatePlan);
        }
    }
}