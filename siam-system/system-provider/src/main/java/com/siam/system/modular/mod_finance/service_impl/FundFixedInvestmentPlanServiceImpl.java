package com.siam.system.modular.mod_finance.service_impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.siam.package_common.constant.BaseCode;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.exception.StoneCustomerException;
import com.siam.package_common.util.OSSUtils;
import com.siam.system.modular.mod_finance.entity.Fund;
import com.siam.system.modular.mod_finance.entity.FundFixedInvestment;
import com.siam.system.modular.mod_finance.entity.FundFixedInvestmentPlan;
import com.siam.system.modular.mod_finance.mapper.FundFixedInvestmentPlanMapper;
import com.siam.system.modular.mod_finance.model.example.FundFixedInvestmentPlanExample;
import com.siam.system.modular.mod_finance.model.param.FundFixedInvestmentPlanParam;
import com.siam.system.modular.mod_finance.service.FundFixedInvestmentPlanService;
import com.siam.system.modular.mod_finance.service.FundFixedInvestmentService;
import com.siam.system.modular.mod_finance.service.FundService;
import com.siam.system.modular.mod_user.auth.cache.MemberSessionManager;
import com.siam.system.modular.mod_user.entity.Member;
import com.siam.system.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FundFixedInvestmentPlanServiceImpl extends ServiceImpl<FundFixedInvestmentPlanMapper, FundFixedInvestmentPlan> implements FundFixedInvestmentPlanService {

    @Autowired
    private FundFixedInvestmentPlanMapper fundFixedInvestmentPlanMapper;

    @Autowired
    private FundFixedInvestmentService fundFixedInvestmentService;

    @Autowired
    private OSSUtils ossUtils;

    @Autowired
    private FundService fundService;

    @Autowired
    private MemberSessionManager memberSessionManager;

    public int countByExample(FundFixedInvestmentPlanExample example){
        return fundFixedInvestmentPlanMapper.countByExample(example);
    }

    @Override
    public void deleteByPrimaryKey(Integer id) {
        fundFixedInvestmentPlanMapper.deleteByPrimaryKey(id);
    }

    public void insertSelective(FundFixedInvestmentPlan record){
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        record.setMemberId(loginMember.getId());
        fundFixedInvestmentPlanMapper.insertSelective(record);
    }

    public List<FundFixedInvestmentPlan> selectByExample(FundFixedInvestmentPlanExample example){
        return fundFixedInvestmentPlanMapper.selectByExample(example);
    }

    public FundFixedInvestmentPlan selectByPrimaryKey(Integer id){
        return fundFixedInvestmentPlanMapper.selectByPrimaryKey(id);
    }

    public void updateByExampleSelective(FundFixedInvestmentPlan record, FundFixedInvestmentPlanExample example){
        fundFixedInvestmentPlanMapper.updateByExampleSelective(record, example);
    }

    public void updateByPrimaryKeySelective(FundFixedInvestmentPlan record){
        fundFixedInvestmentPlanMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Page<FundFixedInvestmentPlan> getListByPage(int pageNo, int pageSize, FundFixedInvestmentPlan fundFixedInvestmentPlan) {
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        fundFixedInvestmentPlan.setMemberId(loginMember.getId());
        Page<FundFixedInvestmentPlan> page = fundFixedInvestmentPlanMapper.getListByPage(new Page(pageNo, pageSize), fundFixedInvestmentPlan);
        return page;
    }

    @Override
    public Page<Map<String, Object>> getMapListByPage(int pageNo, int pageSize, FundFixedInvestmentPlan fundFixedInvestmentPlan) {
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        fundFixedInvestmentPlan.setMemberId(loginMember.getId());
        Page<Map<String, Object>> page = fundFixedInvestmentPlanMapper.getMapListByPage(new Page(pageNo, pageSize), fundFixedInvestmentPlan);
        //获取基金名称
        page.getRecords().forEach(stringObjectMap -> {
            stringObjectMap.put("fundName", getFundNames((String) stringObjectMap.get("fundIdComma")));
        });
        return page;
    }

    /**
     * 获取基金名称
     */
    public String getFundNames(String fundIdComma){
        String result = "";
        String[] arr = fundIdComma.split(",");
        for (String fundId : arr) {
            Fund dbFund = fundService.selectByPrimaryKey(Integer.valueOf(fundId));
            if(result.equals("")){
                result = dbFund.getName();
            }else{
                result = "," + dbFund.getName();
            }
        }
        return result;
    }

    @Override
    public void insert(FundFixedInvestmentPlanParam param) {
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        param.setMemberId(loginMember.getId());
        //添加基金定投计划
        param.setCreateTime(new Date());
        param.setUpdateTime(new Date());
        fundFixedInvestmentPlanMapper.insertSelective(param);
    }

    @Override
    public void update(FundFixedInvestmentPlanParam param) {
        FundFixedInvestmentPlan dbFundFixedInvestmentPlan = fundFixedInvestmentPlanMapper.selectByPrimaryKey(param.getId());
        if(dbFundFixedInvestmentPlan == null){
            throw new StoneCustomerException("该基金定投计划不存在，修改失败");
        }
        //修改基金定投计划
        param.setUpdateTime(new Date());
        fundFixedInvestmentPlanMapper.updateByPrimaryKeySelective(param);
    }

    @Override
    public void delete(FundFixedInvestmentPlanParam param) {
        FundFixedInvestmentPlan dbFundFixedInvestmentPlan = fundFixedInvestmentPlanMapper.selectByPrimaryKey(param.getId());
        if(dbFundFixedInvestmentPlan == null){
            throw new StoneCustomerException("该基金定投计划不存在，删除失败");
        }
        //删除基金定投计划
        param.setUpdateTime(new Date());
        fundFixedInvestmentPlanMapper.deleteByPrimaryKey(param.getId());
    }

    @Override
    public void updateStatus(int id) {
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        FundFixedInvestmentPlan dbPlan = this.getById(id);
        if(dbPlan == null){
            return;
        }
        if(dbPlan.getStatus().equals(FundFixedInvestmentPlan.STATUS_COMPLETED)){
            return;
        }

        //查询定投交易记录总买入记录数量
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("plan_id", id);
        queryWrapper.eq("type", FundFixedInvestment.TYPE_BUY);
        queryWrapper.eq("member_id", loginMember.getId());
        Integer allCount = fundFixedInvestmentService.getBaseMapper().selectCount(queryWrapper);
        //查询定投交易记录买入记录 - 进行中的数量
        queryWrapper.eq("status", FundFixedInvestment.STATUS_IN_PROGRESS);
        queryWrapper.eq("member_id", loginMember.getId());
        Integer inProgressCount = fundFixedInvestmentService.getBaseMapper().selectCount(queryWrapper);

        //获取状态
        int status;
        if(allCount>0 && inProgressCount==0){
            status = FundFixedInvestmentPlan.STATUS_CLEARANCE;
        }else if(allCount>0 && inProgressCount>0){
            status = FundFixedInvestmentPlan.STATUS_IN_PROGRESS;
        }else{
            status = FundFixedInvestmentPlan.STATUS_NOT_STARTED;
        }

        if(!dbPlan.getStatus().equals(status)){
            //修改状态
            FundFixedInvestmentPlan updatePlan = new FundFixedInvestmentPlan();
            updatePlan.setId(id);
            updatePlan.setStatus(status);
            this.updateById(updatePlan);
        }
    }
}