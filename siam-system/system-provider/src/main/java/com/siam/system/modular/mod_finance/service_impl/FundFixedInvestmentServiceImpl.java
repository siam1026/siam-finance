package com.siam.system.modular.mod_finance.service_impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.siam.package_common.constant.Quantity;
import com.siam.package_common.exception.StoneCustomerException;
import com.siam.system.modular.mod_finance.entity.Fund;
import com.siam.system.modular.mod_finance.entity.FundFixedInvestment;
import com.siam.system.modular.mod_finance.mapper.FundFixedInvestmentMapper;
import com.siam.system.modular.mod_finance.model.param.FundFixedInvestmentParam;
import com.siam.system.modular.mod_finance.model.result.FundFixedInvestmentResult;
import com.siam.system.modular.mod_finance.service.FundFixedInvestmentPlanService;
import com.siam.system.modular.mod_finance.service.FundFixedInvestmentService;
import com.siam.system.modular.mod_finance.service.FundService;
import com.siam.system.modular.mod_user.auth.cache.MemberSessionManager;
import com.siam.system.modular.mod_user.entity.Member;
import com.siam.system.util.TokenUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FundFixedInvestmentServiceImpl extends ServiceImpl<FundFixedInvestmentMapper, FundFixedInvestment> implements FundFixedInvestmentService {

    @Autowired
    private FundFixedInvestmentMapper fundFixedInvestmentMapper;

    @Autowired
    private FundService fundService;

    @Autowired
    private FundFixedInvestmentPlanService fundFixedInvestmentPlanService;

    @Autowired
    private MemberSessionManager memberSessionManager;

//    public int countByExample(FundFixedInvestmentExample example){
//        return fundFixedInvestmentMapper.countByExample(example);
//    }

    @Override
    public void delete(FundFixedInvestmentParam param) {
        FundFixedInvestment dbFundFixedInvestment = fundFixedInvestmentMapper.selectById(param.getId());
        if(dbFundFixedInvestment == null){
            throw new StoneCustomerException("该基金定投交易记录不存在，删除失败");
        }

        //删除基金定投交易记录
        fundFixedInvestmentMapper.deleteById(param.getId());

        //级联更新定投计划状态
        fundFixedInvestmentPlanService.updateStatus(dbFundFixedInvestment.getPlanId());
    }

    public void insert(FundFixedInvestmentParam param){
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        param.setMemberId(loginMember.getId());
        if(param.getType() == Quantity.INT_0){
            //定投买入操作
            //添加基金定投交易记录
            param.setStatus(Quantity.INT_1);
            param.setUpdateTime(new Date());
            fundFixedInvestmentMapper.insert(param);
        } else if(param.getType() == Quantity.INT_1){
            //定投卖出操作
            //此处不需要计算收益/收益率等数据，应该是前端点击一个定投结算按钮进行收益计算(因为如果行情好的话，我可能会多投资一段时间/加大投资金额)
            //添加基金定投交易记录
            param.setStatus(Quantity.INT_0);
            param.setUpdateTime(new Date());
            fundFixedInvestmentMapper.insert(param);
        }

        //级联更新定投计划状态
        fundFixedInvestmentPlanService.updateStatus(param.getPlanId());
    }

//    public List<FundFixedInvestment> selectByExample(FundFixedInvestmentExample example){
//        return fundFixedInvestmentMapper.selectByExample(example);
//    }
//
//    public FundFixedInvestment selectByPrimaryKey(Integer id){
//        return fundFixedInvestmentMapper.selectByPrimaryKey(id);
//    }
//
//    public void updateByExampleSelective(FundFixedInvestment record, FundFixedInvestmentExample example){
//        fundFixedInvestmentMapper.updateByExampleSelective(record, example);
//    }

    public void update(FundFixedInvestmentParam param){
        FundFixedInvestment dbFundFixedInvestment = fundFixedInvestmentMapper.selectById(param.getId());
        if(dbFundFixedInvestment == null){
            throw new StoneCustomerException("该基金定投交易记录不存在，修改失败");
        }

        //修改基金定投交易记录
        param.setUpdateTime(new Date());
        fundFixedInvestmentMapper.updateById(param);

        //级联更新定投计划状态
        fundFixedInvestmentPlanService.updateStatus(dbFundFixedInvestment.getPlanId());
    }

    @Override
    public Page<FundFixedInvestment> getListByPage(int pageNo, int pageSize, FundFixedInvestment fundFixedInvestment) {
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        fundFixedInvestment.setMemberId(loginMember.getId());
        Page<FundFixedInvestment> page = fundFixedInvestmentMapper.getListByPage(new Page(pageNo, pageSize), fundFixedInvestment);
        return page;
    }

    @Override
    public Page<Map<String, Object>> getMapListByPageJoinFund(FundFixedInvestmentParam param) {
        //TODO(MARK) - 定投卖出只能是以整份的形式卖出
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        param.setMemberId(loginMember.getId());
        Page<Map<String, Object>> page = fundFixedInvestmentMapper.getMapListByPageJoinFund(new Page(param.getPageNo(), param.getPageSize()), param);

        //对进行中的做T交易买入记录进行分析
        if(param.getType()!=null && param.getType()==Quantity.INT_0){
            //卖出手续费比例暂时按照0.5%来算
            BigDecimal serviceChargeRatio = BigDecimal.valueOf(0.5);

            //计算当日做T卖出可获收益(已扣除手续费),手续费,收益率
            page.getRecords().forEach(map -> {
                Fund dbFund = fundService.selectByPrimaryKey((int) map.get("fundId"));

                //计算规则：收益 = (基金最新净值 * 做T买入份额) - 手续费 - 做T买入金额
                BigDecimal sellIncome = dbFund.getLastestNetValue().multiply(((BigDecimal) map.get("shares"))).setScale(2, BigDecimal.ROUND_HALF_UP);
                //手续费  注意手续费比例要除以100
                BigDecimal sellServiceCharge = sellIncome.multiply(serviceChargeRatio.divide(BigDecimal.valueOf(100))).setScale(2, BigDecimal.ROUND_HALF_UP);
                sellIncome = sellIncome.subtract(sellServiceCharge).subtract(((BigDecimal) map.get("amount")));
                //收益率 此处除的时候要保留4位小数，因为后面会乘以100
                BigDecimal sellReturnRate = sellIncome.divide(((BigDecimal) map.get("amount")), 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
                map.put("sellIncome", sellIncome);
                map.put("sellServiceCharge", sellServiceCharge);
                map.put("sellReturnRate", sellReturnRate);
            });
        }

        //卖出记录
        if(param.getType()!=null && param.getType()==Quantity.INT_1){
            page.getRecords().forEach(map -> {
                //查询关联的买入交易记录数量
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("related_trading_id", (int) map.get("id"));
                queryWrapper.eq("member_id", loginMember.getId());
                int relatedTradingNum = this.count(queryWrapper);
                map.put("relatedTradingNum", relatedTradingNum);
            });
        }
        return page;
    }

    @Override
    public void bindRelatedTrading(FundFixedInvestmentParam param) {
        FundFixedInvestment dbFundFixedInvestment = fundFixedInvestmentMapper.selectById(param.getId());
        if(dbFundFixedInvestment == null){
            throw new StoneCustomerException("该基金定投交易记录不存在，修改失败");
        }

        //解除以前的绑定关系
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("related_trading_id", param.getId());
        FundFixedInvestment updateFundFixedInvestment = new FundFixedInvestment();
        updateFundFixedInvestment.setRelatedTradingId(Quantity.INT_MINUS_1);
        updateFundFixedInvestment.setStatus(Quantity.INT_1);
        this.update(updateFundFixedInvestment, updateWrapper);

        //重新进行绑定
        updateWrapper = new UpdateWrapper();
        updateWrapper.eq("type", Quantity.INT_0);
//        updateWrapper.eq("status", Quantity.INT_1);
        updateWrapper.in("id", param.getRelatedTradingIdList());
        updateFundFixedInvestment = new FundFixedInvestment();
        updateFundFixedInvestment.setRelatedTradingId(param.getId());
        updateFundFixedInvestment.setStatus(Quantity.INT_2);
        this.update(updateFundFixedInvestment, updateWrapper);

        //计算卖出记录的卖出收益率
        BigDecimal sumShares = BigDecimal.ZERO;
        BigDecimal sumAmount = BigDecimal.ZERO;
        List<Integer> relatedTradingIdList = param.getRelatedTradingIdList();
        for (Integer relatedTradingId : relatedTradingIdList) {
            FundFixedInvestment relatedTrading = this.getById(relatedTradingId);
            sumShares = sumShares.add(relatedTrading.getShares());
            sumAmount = sumAmount.add(relatedTrading.getAmount());
        }
        if(sumShares.compareTo(dbFundFixedInvestment.getShares()) != 0){
            throw new StoneCustomerException("份额数据与卖出记录不一致，请重新绑定");
        }
        BigDecimal totalIncome = dbFundFixedInvestment.getAmount().subtract(sumAmount);
        BigDecimal totalReturnRate = totalIncome.divide(sumAmount, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
        updateFundFixedInvestment = new FundFixedInvestment();
        updateFundFixedInvestment.setId(dbFundFixedInvestment.getId());
        updateFundFixedInvestment.setIncome(totalIncome);
        updateFundFixedInvestment.setReturnRate(totalReturnRate);
        this.updateById(updateFundFixedInvestment);

        //级联更新定投计划状态
        fundFixedInvestmentPlanService.updateStatus(dbFundFixedInvestment.getPlanId());
    }

    @Override
    public FundFixedInvestmentResult detail(FundFixedInvestmentParam param) {
        FundFixedInvestmentResult result = new FundFixedInvestmentResult();
        FundFixedInvestment entity = this.getById(param.getId());
        BeanUtils.copyProperties(entity, result);

        //查询关联的买入交易记录id列表
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("related_trading_id", param.getId());
        List relatedTradingList = this.list(queryWrapper);
        result.setRelatedTradingList(relatedTradingList);

        return result;
    }
}