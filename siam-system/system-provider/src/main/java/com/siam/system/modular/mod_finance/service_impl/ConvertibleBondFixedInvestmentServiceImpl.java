package com.siam.system.modular.mod_finance.service_impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.siam.package_common.constant.Quantity;
import com.siam.package_common.exception.StoneCustomerException;
import com.siam.system.modular.mod_finance.entity.ConvertibleBond;
import com.siam.system.modular.mod_finance.entity.ConvertibleBondFixedInvestment;
import com.siam.system.modular.mod_finance.mapper.ConvertibleBondFixedInvestmentMapper;
import com.siam.system.modular.mod_finance.model.param.ConvertibleBondFixedInvestmentParam;
import com.siam.system.modular.mod_finance.model.result.ConvertibleBondFixedInvestmentResult;
import com.siam.system.modular.mod_finance.service.ConvertibleBondFixedInvestmentPlanService;
import com.siam.system.modular.mod_finance.service.ConvertibleBondService;
import com.siam.system.modular.mod_user.auth.cache.MemberSessionManager;
import com.siam.system.modular.mod_user.entity.Member;
import com.siam.system.util.TokenUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.siam.system.modular.mod_finance.service.ConvertibleBondFixedInvestmentService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 可转债定投交易记录表业务实现层
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
@Service
public class ConvertibleBondFixedInvestmentServiceImpl extends ServiceImpl<ConvertibleBondFixedInvestmentMapper, ConvertibleBondFixedInvestment> implements ConvertibleBondFixedInvestmentService {

    @Autowired
    private ConvertibleBondFixedInvestmentMapper convertibleBondFixedInvestmentMapper;

    @Autowired
    private ConvertibleBondService convertibleBondService;

    @Autowired
    private ConvertibleBondFixedInvestmentPlanService convertibleBondFixedInvestmentPlanService;

    @Autowired
    private MemberSessionManager memberSessionManager;

//    public int countByExample(ConvertibleBondFixedInvestmentExample example){
//        return convertibleBondFixedInvestmentMapper.countByExample(example);
//    }

    @Override
    public void delete(ConvertibleBondFixedInvestmentParam param) {
        ConvertibleBondFixedInvestment dbConvertibleBondFixedInvestment = convertibleBondFixedInvestmentMapper.selectById(param.getId());
        if(dbConvertibleBondFixedInvestment == null){
            throw new StoneCustomerException("该基金定投交易记录不存在，删除失败");
        }

        //删除基金定投交易记录
        convertibleBondFixedInvestmentMapper.deleteById(param.getId());

        //级联更新定投计划状态
        convertibleBondFixedInvestmentPlanService.updateStatus(dbConvertibleBondFixedInvestment.getPlanId());
    }

    public void insert(ConvertibleBondFixedInvestmentParam param){
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        param.setMemberId(loginMember.getId());
        if(param.getType() == Quantity.INT_0){
            //定投买入操作
            //添加基金定投交易记录
            param.setStatus(Quantity.INT_1);
            param.setUpdateTime(new Date());
            convertibleBondFixedInvestmentMapper.insert(param);
        } else if(param.getType() == Quantity.INT_1){
            //定投卖出操作
            //此处不需要计算收益/收益率等数据，应该是前端点击一个定投结算按钮进行收益计算(因为如果行情好的话，我可能会多投资一段时间/加大投资金额)
            //添加基金定投交易记录
            param.setStatus(Quantity.INT_0);
            param.setUpdateTime(new Date());
            convertibleBondFixedInvestmentMapper.insert(param);
        }

        //级联更新定投计划状态
        convertibleBondFixedInvestmentPlanService.updateStatus(param.getPlanId());
    }

//    public List<ConvertibleBondFixedInvestment> selectByExample(ConvertibleBondFixedInvestmentExample example){
//        return convertibleBondFixedInvestmentMapper.selectByExample(example);
//    }
//
//    public ConvertibleBondFixedInvestment selectByPrimaryKey(Integer id){
//        return convertibleBondFixedInvestmentMapper.selectByPrimaryKey(id);
//    }
//
//    public void updateByExampleSelective(ConvertibleBondFixedInvestment record, ConvertibleBondFixedInvestmentExample example){
//        convertibleBondFixedInvestmentMapper.updateByExampleSelective(record, example);
//    }

    public void update(ConvertibleBondFixedInvestmentParam param){
        ConvertibleBondFixedInvestment dbConvertibleBondFixedInvestment = convertibleBondFixedInvestmentMapper.selectById(param.getId());
        if(dbConvertibleBondFixedInvestment == null){
            throw new StoneCustomerException("该基金定投交易记录不存在，修改失败");
        }

        //修改基金定投交易记录
        param.setUpdateTime(new Date());
        convertibleBondFixedInvestmentMapper.updateById(param);

        //级联更新定投计划状态
        convertibleBondFixedInvestmentPlanService.updateStatus(dbConvertibleBondFixedInvestment.getPlanId());
    }

    @Override
    public Page<ConvertibleBondFixedInvestment> getListByPage(int pageNo, int pageSize, ConvertibleBondFixedInvestment convertibleBondFixedInvestment) {
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        convertibleBondFixedInvestment.setMemberId(loginMember.getId());
        Page<ConvertibleBondFixedInvestment> page = convertibleBondFixedInvestmentMapper.getListByPage(new Page(pageNo, pageSize), convertibleBondFixedInvestment);
        return page;
    }

    @Override
    public Page<Map<String, Object>> getMapListByPageJoinConvertibleBond(ConvertibleBondFixedInvestmentParam param) {
        //TODO(MARK) - 定投卖出只能是以整份的形式卖出
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        param.setMemberId(loginMember.getId());
        Page<Map<String, Object>> page = convertibleBondFixedInvestmentMapper.getMapListByPageJoinConvertibleBond(new Page(param.getPageNo(), param.getPageSize()), param);

        //对进行中的做T交易买入记录进行分析
        if(param.getType()!=null && param.getType()==Quantity.INT_0){
            //卖出手续费比例暂时按照0.5%来算
            BigDecimal serviceChargeRatio = BigDecimal.valueOf(0.5);

            //计算当日做T卖出可获收益(已扣除手续费),手续费,收益率
            page.getRecords().forEach(map -> {
                ConvertibleBond dbConvertibleBond = convertibleBondService.getById((int) map.get("convertibleBondId"));

                //计算规则：收益 = (基金最新净值 * 做T买入份额) - 手续费 - 做T买入金额
                BigDecimal sellIncome = dbConvertibleBond.getLastestPrice().multiply(((BigDecimal) map.get("quantity"))).setScale(2, BigDecimal.ROUND_HALF_UP);
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
    public void bindRelatedTrading(ConvertibleBondFixedInvestmentParam param) {
        ConvertibleBondFixedInvestment dbConvertibleBondFixedInvestment = convertibleBondFixedInvestmentMapper.selectById(param.getId());
        if(dbConvertibleBondFixedInvestment == null){
            throw new StoneCustomerException("该基金定投交易记录不存在，修改失败");
        }

        //解除以前的绑定关系
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("related_trading_id", param.getId());
        ConvertibleBondFixedInvestment updateConvertibleBondFixedInvestment = new ConvertibleBondFixedInvestment();
        updateConvertibleBondFixedInvestment.setRelatedTradingId(Quantity.INT_MINUS_1);
        updateConvertibleBondFixedInvestment.setStatus(Quantity.INT_1);
        this.update(updateConvertibleBondFixedInvestment, updateWrapper);

        //重新进行绑定
        updateWrapper = new UpdateWrapper();
        updateWrapper.eq("type", Quantity.INT_0);
//        updateWrapper.eq("status", Quantity.INT_1);
        updateWrapper.in("id", param.getRelatedTradingIdList());
        updateConvertibleBondFixedInvestment = new ConvertibleBondFixedInvestment();
        updateConvertibleBondFixedInvestment.setRelatedTradingId(param.getId());
        updateConvertibleBondFixedInvestment.setStatus(Quantity.INT_2);
        this.update(updateConvertibleBondFixedInvestment, updateWrapper);

        //计算卖出记录的卖出收益率
        BigDecimal sumShares = BigDecimal.ZERO;
        BigDecimal sumAmount = BigDecimal.ZERO;
        List<Integer> relatedTradingIdList = param.getRelatedTradingIdList();
        for (Integer relatedTradingId : relatedTradingIdList) {
            ConvertibleBondFixedInvestment relatedTrading = this.getById(relatedTradingId);
            sumShares = sumShares.add(relatedTrading.getQuantity());
            sumAmount = sumAmount.add(relatedTrading.getAmount());
        }
        if(sumShares.compareTo(dbConvertibleBondFixedInvestment.getQuantity()) != 0){
            throw new StoneCustomerException("份额数据与卖出记录不一致，请重新绑定");
        }
        BigDecimal totalIncome = dbConvertibleBondFixedInvestment.getAmount().subtract(sumAmount);
        BigDecimal totalReturnRate = totalIncome.divide(sumAmount, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
        updateConvertibleBondFixedInvestment = new ConvertibleBondFixedInvestment();
        updateConvertibleBondFixedInvestment.setId(dbConvertibleBondFixedInvestment.getId());
        updateConvertibleBondFixedInvestment.setIncome(totalIncome);
        updateConvertibleBondFixedInvestment.setReturnRate(totalReturnRate);
        this.updateById(updateConvertibleBondFixedInvestment);

        //级联更新定投计划状态
        convertibleBondFixedInvestmentPlanService.updateStatus(dbConvertibleBondFixedInvestment.getPlanId());
    }

    @Override
    public ConvertibleBondFixedInvestmentResult detail(ConvertibleBondFixedInvestmentParam param) {
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());

        ConvertibleBondFixedInvestmentResult result = new ConvertibleBondFixedInvestmentResult();
        ConvertibleBondFixedInvestment entity = this.getById(param.getId());
        BeanUtils.copyProperties(entity, result);

        //查询关联的买入交易记录id列表
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("related_trading_id", param.getId());
        queryWrapper.eq("member_id", loginMember.getId());
        List relatedTradingList = this.list(queryWrapper);
        result.setRelatedTradingList(relatedTradingList);

        return result;
    }
}