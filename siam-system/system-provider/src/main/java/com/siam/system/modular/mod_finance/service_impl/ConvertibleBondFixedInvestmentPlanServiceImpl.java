package com.siam.system.modular.mod_finance.service_impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.siam.package_common.exception.StoneCustomerException;
import com.siam.package_common.util.OSSUtils;
import com.siam.system.modular.mod_finance.entity.*;
import com.siam.system.modular.mod_finance.mapper.ConvertibleBondFixedInvestmentPlanMapper;
import com.siam.system.modular.mod_finance.model.param.ConvertibleBondFixedInvestmentPlanParam;
import com.siam.system.modular.mod_finance.service.ConvertibleBondFixedInvestmentService;
import com.siam.system.modular.mod_finance.service.ConvertibleBondService;
import com.siam.system.modular.mod_user.auth.cache.MemberSessionManager;
import com.siam.system.modular.mod_user.entity.Member;
import com.siam.system.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.siam.system.modular.mod_finance.entity.ConvertibleBondFixedInvestmentPlan;
import com.siam.system.modular.mod_finance.service.ConvertibleBondFixedInvestmentPlanService;

import java.util.Date;
import java.util.Map;

/**
 * 可转债定投计划表业务实现层
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
@Service
public class ConvertibleBondFixedInvestmentPlanServiceImpl extends ServiceImpl<ConvertibleBondFixedInvestmentPlanMapper, ConvertibleBondFixedInvestmentPlan> implements ConvertibleBondFixedInvestmentPlanService {

    @Autowired
    private ConvertibleBondFixedInvestmentPlanMapper convertibleBondFixedInvestmentPlanMapper;

    @Autowired
    private OSSUtils ossUtils;

    @Autowired
    private ConvertibleBondService convertibleBondService;

    @Autowired
    private ConvertibleBondFixedInvestmentService convertibleBondFixedInvestmentService;

    @Autowired
    private MemberSessionManager memberSessionManager;

    @Override
    public Page<ConvertibleBondFixedInvestmentPlan> getListByPage(int pageNo, int pageSize, ConvertibleBondFixedInvestmentPlan convertibleBondFixedInvestmentPlan) {
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        convertibleBondFixedInvestmentPlan.setMemberId(loginMember.getId());
        Page<ConvertibleBondFixedInvestmentPlan> page = convertibleBondFixedInvestmentPlanMapper.getListByPage(new Page(pageNo, pageSize), convertibleBondFixedInvestmentPlan);
        return page;
    }

    @Override
    public Page<Map<String, Object>> getMapListByPage(int pageNo, int pageSize, ConvertibleBondFixedInvestmentPlan convertibleBondFixedInvestmentPlan) {
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        convertibleBondFixedInvestmentPlan.setMemberId(loginMember.getId());

        Page<Map<String, Object>> page = convertibleBondFixedInvestmentPlanMapper.getMapListByPage(new Page(pageNo, pageSize), convertibleBondFixedInvestmentPlan);
        //获取基金名称
        page.getRecords().forEach(stringObjectMap -> {
            stringObjectMap.put("convertibleBondName", getConvertibleBondNames((String) stringObjectMap.get("convertibleBondIdComma")));
        });
        return page;
    }

    /**
     * 获取基金名称
     */
    public String getConvertibleBondNames(String convertibleBondIdComma){
        String result = "";
        String[] arr = convertibleBondIdComma.split(",");
        for (String convertibleBondId : arr) {
            ConvertibleBond dbConvertibleBond = convertibleBondService.getById(Integer.valueOf(convertibleBondId));
            if(result.equals("")){
                result = dbConvertibleBond.getName();
            }else{
                result = "," + dbConvertibleBond.getName();
            }
        }
        return result;
    }

    @Override
    public void insert(ConvertibleBondFixedInvestmentPlanParam param) {
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        param.setMemberId(loginMember.getId());
        //添加基金定投计划
        param.setCreateTime(new Date());
        param.setUpdateTime(new Date());
        convertibleBondFixedInvestmentPlanMapper.insert(param);
    }

    @Override
    public void update(ConvertibleBondFixedInvestmentPlanParam param) {
        ConvertibleBondFixedInvestmentPlan dbConvertibleBondFixedInvestmentPlan = convertibleBondFixedInvestmentPlanMapper.selectById(param.getId());
        if(dbConvertibleBondFixedInvestmentPlan == null){
            throw new StoneCustomerException("该基金定投计划不存在，修改失败");
        }
        //修改基金定投计划
        param.setUpdateTime(new Date());
        convertibleBondFixedInvestmentPlanMapper.updateById(param);
    }

    @Override
    public void delete(ConvertibleBondFixedInvestmentPlanParam param) {
        ConvertibleBondFixedInvestmentPlan dbConvertibleBondFixedInvestmentPlan = convertibleBondFixedInvestmentPlanMapper.selectById(param.getId());
        if(dbConvertibleBondFixedInvestmentPlan == null){
            throw new StoneCustomerException("该基金定投计划不存在，删除失败");
        }
        //删除基金定投计划
        param.setUpdateTime(new Date());
        convertibleBondFixedInvestmentPlanMapper.deleteById(param.getId());
    }

    @Override
    public void updateStatus(int id) {
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        ConvertibleBondFixedInvestmentPlan dbPlan = this.getById(id);
        if(dbPlan == null){
            return;
        }
        if(dbPlan.getStatus().equals(ConvertibleBondFixedInvestmentPlan.STATUS_COMPLETED)){
            return;
        }

        //查询定投交易记录总买入记录数量
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("plan_id", id);
        queryWrapper.eq("type", ConvertibleBondFixedInvestment.TYPE_BUY);
        queryWrapper.eq("member_id", loginMember.getId());
        Integer allCount = convertibleBondFixedInvestmentService.getBaseMapper().selectCount(queryWrapper);
        //查询定投交易记录买入记录 - 进行中的数量
        queryWrapper.eq("status", ConvertibleBondFixedInvestment.STATUS_IN_PROGRESS);
        queryWrapper.eq("member_id", loginMember.getId());
        Integer inProgressCount = convertibleBondFixedInvestmentService.getBaseMapper().selectCount(queryWrapper);

        //获取状态
        int status;
        if(allCount>0 && inProgressCount==0){
            status = ConvertibleBondFixedInvestmentPlan.STATUS_CLEARANCE;
        }else if(allCount>0 && inProgressCount>0){
            status = ConvertibleBondFixedInvestmentPlan.STATUS_IN_PROGRESS;
        }else{
            status = ConvertibleBondFixedInvestmentPlan.STATUS_NOT_STARTED;
        }

        if(!dbPlan.getStatus().equals(status)){
            //修改状态
            ConvertibleBondFixedInvestmentPlan updatePlan = new ConvertibleBondFixedInvestmentPlan();
            updatePlan.setId(id);
            updatePlan.setStatus(status);
            this.updateById(updatePlan);
        }
    }
}