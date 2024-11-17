package com.siam.system.modular.mod_finance.service_impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.siam.package_common.constant.BaseCode;
import com.siam.package_common.constant.Quantity;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.exception.StoneCustomerException;
import com.siam.system.modular.mod_finance.entity.Fund;
import com.siam.system.modular.mod_finance.entity.FundShorttermTrading;
import com.siam.system.modular.mod_finance.model.example.FundShorttermTradingExample;
import com.siam.system.modular.mod_finance.mapper.FundShorttermTradingMapper;
import com.siam.system.modular.mod_finance.model.param.FundShorttermTradingParam;
import com.siam.system.modular.mod_finance.service.FundService;
import com.siam.system.modular.mod_finance.service.FundShorttermTradingService;
import com.siam.system.modular.mod_user.auth.cache.MemberSessionManager;
import com.siam.system.modular.mod_user.entity.Member;
import com.siam.system.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FundShorttermTradingServiceImpl extends ServiceImpl<FundShorttermTradingMapper, FundShorttermTrading> implements FundShorttermTradingService {

    @Autowired
    private FundShorttermTradingMapper fundShorttermTradingMapper;

    @Autowired
    private FundService fundService;

    @Autowired
    private MemberSessionManager memberSessionManager;

    public int countByExample(FundShorttermTradingExample example){
        return fundShorttermTradingMapper.countByExample(example);
    }

    @Override
    public void deleteByPrimaryKey(Integer id) {
        fundShorttermTradingMapper.deleteByPrimaryKey(id);
    }

    public void insertSelective(FundShorttermTrading record){
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        record.setMemberId(loginMember.getId());
        fundShorttermTradingMapper.insertSelective(record);
    }

    public List<FundShorttermTrading> selectByExample(FundShorttermTradingExample example){
        return fundShorttermTradingMapper.selectByExample(example);
    }

    public FundShorttermTrading selectByPrimaryKey(Integer id){
        return fundShorttermTradingMapper.selectByPrimaryKey(id);
    }

    public void updateByExampleSelective(FundShorttermTrading record, FundShorttermTradingExample example){
        fundShorttermTradingMapper.updateByExampleSelective(record, example);
    }

    public void updateByPrimaryKeySelective(FundShorttermTrading record){
        fundShorttermTradingMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Page<FundShorttermTrading> getListByPage(int pageNo, int pageSize, FundShorttermTrading fundShorttermTrading) {
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        fundShorttermTrading.setMemberId(loginMember.getId());
        Page<FundShorttermTrading> page = fundShorttermTradingMapper.getListByPage(new Page(pageNo, pageSize), fundShorttermTrading);
        return page;
    }

    public Page<Map<String, Object>> getListByPageJoinFund(int pageNo, int pageSize, FundShorttermTradingParam param){
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        param.setMemberId(loginMember.getId());
        Page<Map<String, Object>> page = fundShorttermTradingMapper.getListByPageJoinFund(new Page(param.getPageNo(), param.getPageSize()), param);

        //对进行中的做T交易买入记录进行分析
        if(param.getType()!=null && param.getStatus()!=null && param.getType()== Quantity.INT_0 && param.getStatus()==Quantity.INT_1){
            //卖出手续费比例暂时按照0.5%来算
            BigDecimal serviceChargeRatio = BigDecimal.valueOf(0.5);

            //计算剩余份额、当日做T卖出可获收益(已扣除手续费),手续费,收益率
            page.getRecords().forEach(map -> {
                Fund dbFund = fundService.selectByPrimaryKey((int) map.get("fundId"));

                //剩余份额
                BigDecimal remainingShares = ((BigDecimal) map.get("shares")).subtract(fundShorttermTradingMapper.selectSumSellSharesByRelatedTradingId((int) map.get("id")));

                //剩余份额百分比
                BigDecimal remainingSharesRate = remainingShares.divide((BigDecimal) map.get("shares"), 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));

                //计算规则：收益 = (基金最新净值 * 做T买入份额) - 手续费 - 做T买入金额
                BigDecimal sellIncome = dbFund.getLastestNetValue().multiply(((BigDecimal) map.get("shares"))).setScale(2, BigDecimal.ROUND_HALF_UP);
                //手续费  注意手续费比例要除以100
                BigDecimal sellServiceCharge = sellIncome.multiply(serviceChargeRatio.divide(BigDecimal.valueOf(100))).setScale(2, BigDecimal.ROUND_HALF_UP);
                sellIncome = sellIncome.subtract(sellServiceCharge).subtract(((BigDecimal) map.get("amount")));

                //收益率 此处除的时候要保留4位小数，因为后面会乘以100
                BigDecimal sellReturnRate = sellIncome.divide(((BigDecimal) map.get("amount")), 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));

                map.put("remainingShares", remainingShares);
                map.put("remainingSharesRate", remainingSharesRate);
                map.put("sellIncome", sellIncome);
                map.put("sellServiceCharge", sellServiceCharge);
                map.put("sellReturnRate", sellReturnRate);
            });
        }
        return page;
    }

    /**
     * 此方法已废弃，这是最开始的一种写法
     * @deprecated
     * @return
     */
    /*public Page<Map<String, Object>> getListByPageJoinFund(int pageNo, int pageSize, FundShorttermTradingParam param){
        Page<Map<String, Object>> page = fundShorttermTradingMapper.getListByPageJoinFund(new Page(param.getPageNo(), param.getPageSize()), param);

        //对进行中的做T交易买入记录进行分析
        if(param.getType()!=null && param.getStatus()!=null && param.getType()==Quantity.INT_0 && param.getStatus()==Quantity.INT_1){
            //卖出手续费比例暂时按照0.5%来算
            BigDecimal serviceChargeRatio = BigDecimal.valueOf(0.5);

            //计算剩余份额、当日做T卖出可获收益(已扣除手续费),手续费,收益率
            for (int i = 0; i < page.getRecords().size(); i++) {
                FundShorttermTrading dbFundShorttermTrading = page.getRecords().get(i);

                Fund dbFund = fundService.detail(dbFundShorttermTrading.getFundId());

                //剩余份额
                BigDecimal remainingShares = dbFundShorttermTrading.getShares().subtract(fundShorttermTradingMapper.selectSumSellSharesByRelatedTradingId(dbFundShorttermTrading.getRelatedTradingId()));

                //计算规则：收益 = (基金最新净值 * 做T买入份额) - 手续费 - 做T买入金额
                BigDecimal income = dbFund.getLastestNetValue().multiply(dbFundShorttermTrading.getShares()).setScale(2, BigDecimal.ROUND_HALF_UP);
                //手续费  注意手续费比例要除以100
                BigDecimal serviceCharge = income.multiply(serviceChargeRatio.divide(BigDecimal.valueOf(100))).setScale(2, BigDecimal.ROUND_HALF_UP);
                income = income.subtract(serviceCharge).subtract(dbFundShorttermTrading.getAmount());

                //收益率
                BigDecimal returnRate = income.divide(dbFundShorttermTrading.getAmount(), 2, BigDecimal.ROUND_HALF_UP);

                dbFundShorttermTrading.setIncome(income);

                FundShorttermTradingDto fundShorttermTradingDto = new FundShorttermTradingDto();
                fundShorttermTradingDto.setRemainingShares(remainingShares);
                fundShorttermTradingDto.setServiceCharge(serviceCharge);
                fundShorttermTradingDto.setReturnRate(returnRate);
                BeanUtils.copyProperties(dbFundShorttermTrading, fundShorttermTradingDto);

                //替换list集合中的元素  注意不是add()--是set()
                page.getRecords().set(i, fundShorttermTradingDto);
            }
        }
        return page;
    }*/

    @Override
    public BigDecimal selectSumSellSharesByRelatedTradingId(Integer relatedTradingId) {
        return fundShorttermTradingMapper.selectSumSellSharesByRelatedTradingId(relatedTradingId);
    }

    @Override
    public BigDecimal selectSumSellAmountByRelatedTradingId(Integer relatedTradingId) {
        return fundShorttermTradingMapper.selectSumSellAmountByRelatedTradingId(relatedTradingId);
    }

    @Override
    public void insert(FundShorttermTradingParam param) {
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        param.setMemberId(loginMember.getId());

        //如果是卖出操作，则需要计算 做T卖出操作所获收益，并且改变 做T状态
        if(param.getType() == Quantity.INT_1){
            //找到做T卖出操作关联的做T买入交易id
            FundShorttermTrading relatedTrading = fundShorttermTradingMapper.selectByPrimaryKey(param.getRelatedTradingId());
            if(relatedTrading == null){
                throw new StoneCustomerException("关联的做T买入交易记录不存在，添加失败");
            }else if(relatedTrading.getStatus() == Quantity.INT_2){
                throw new StoneCustomerException("关联的做T买入交易记录处于已完成状态，添加失败");
            }
            //1、计算单笔卖出所获收益/收益率
            //该笔做T买入交易所获收益 = 卖出金额 - (卖出份额 * 买入时净值)
            //BigDecimal sumAmount = fundShorttermTrading.getAmount().add(fundShorttermTradingMapper.selectSumAmountByRelatedTradingId(relatedTrading.getId()));
            //BigDecimal income = sumAmount.subtract(relatedTrading.getAmount());
            BigDecimal cost = param.getShares().multiply(relatedTrading.getNetValue());
            BigDecimal income = param.getAmount().subtract(cost);

            //该笔卖出操作收益率 此处除的时候要保留4位小数，因为后面会乘以100
            BigDecimal returnRate = income.divide(cost, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));

            //关联写入基金id，卖出操作的状态要设置为无
            param.setFundId(relatedTrading.getFundId());
            param.setStatus(Quantity.INT_0);
            param.setIncome(income);
            param.setReturnRate(returnRate);
            //2 添加基金短期交易/做T交易记录
            //fundShorttermTrading.setCreateTime(new Date());
            param.setUpdateTime(new Date());
            fundShorttermTradingMapper.insertSelective(param);

            //2、计算总做T记录卖出所获收益/收益率
            //当该笔做T记录完成时才进行总收益/总收益率计算，计算时需要减去买入手续费
            //查询该关联的做T买入交易记录的所有卖出份额 是否与 买入份额相等，如果相等则代表该笔做T买入交易已完成
            BigDecimal sumSellShares = fundShorttermTradingMapper.selectSumSellSharesByRelatedTradingId(relatedTrading.getId());
            if(sumSellShares.compareTo(relatedTrading.getShares()) == 0){
                //总收益率 此处除的时候要保留4位小数，因为后面会乘以100
                //当该笔做T记录完成时才进行总收益/总收益率计算，计算时需要减去买入手续费
                //总收益不能用份额*净值来计算，要以实际买入金额-累计实际卖出金额来计算
                /*BigDecimal totalCost = totalShares.multiply(relatedTrading.getNetValue());
                BigDecimal totalIncome = relatedTrading.getIncome().add(income);
                BigDecimal totalReturnRate = totalIncome.divide(totalCost, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));*/

                BigDecimal sumSellAmount = fundShorttermTradingMapper.selectSumSellAmountByRelatedTradingId(relatedTrading.getId());
                BigDecimal totalIncome = sumSellAmount.subtract(relatedTrading.getAmount());
                BigDecimal totalReturnRate = totalIncome.divide(relatedTrading.getAmount(), 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));

                //将做T状态改为已完成
                //修改关联的做T买入交易记录
                FundShorttermTrading updateRelatedTrading = new FundShorttermTrading();
                updateRelatedTrading.setId(relatedTrading.getId());
                updateRelatedTrading.setStatus(Quantity.INT_2);
                updateRelatedTrading.setIncome(totalIncome);
                updateRelatedTrading.setReturnRate(totalReturnRate);
                fundShorttermTradingMapper.updateByPrimaryKeySelective(updateRelatedTrading);
            }

        }else{
            //买入操作的状态要设置为进行中
            param.setStatus(Quantity.INT_1);
            //2 添加基金短期交易/做T交易记录
            //fundShorttermTrading.setCreateTime(new Date());
            param.setUpdateTime(new Date());
            fundShorttermTradingMapper.insertSelective(param);
        }
    }

    @Override
    public void update(FundShorttermTradingParam param) {
        FundShorttermTrading dbFundShorttermTrading = fundShorttermTradingMapper.selectByPrimaryKey(param.getId());
        if(dbFundShorttermTrading == null){
            throw new StoneCustomerException("该基金短期交易/做T交易记录不存在，修改失败");
        }

        //如果是卖出操作，则需要重新计算 做T卖出操作所获收益
        if(dbFundShorttermTrading.getType() == Quantity.INT_1){
            //找到做T卖出操作关联的做T买入交易id
            FundShorttermTrading relatedTrading = fundShorttermTradingMapper.selectByPrimaryKey(dbFundShorttermTrading.getRelatedTradingId());
            if(relatedTrading == null){
                throw new StoneCustomerException("关联的做T买入交易记录不存在，修改失败");
            }else if(relatedTrading.getStatus() == Quantity.INT_2){
                throw new StoneCustomerException("关联的做T买入交易记录处于已完成状态，修改失败");
            }

            //1 修改关联的做T买入交易记录
            FundShorttermTrading updateRelatedTrading = new FundShorttermTrading();
            updateRelatedTrading.setId(relatedTrading.getId());

            //查询该关联的做T买入交易记录的所有卖出份额 是否与 买入份额相等，如果相等则代表该笔做T买入交易已完成
            BigDecimal totalShares = param.getShares().add(fundShorttermTradingMapper.selectSumSellSharesByRelatedTradingId(relatedTrading.getId())).subtract(dbFundShorttermTrading.getShares());
            if(totalShares.compareTo(relatedTrading.getShares()) == 0){
                //将做T状态改为已完成
                updateRelatedTrading.setStatus(Quantity.INT_2);
            }

            //该笔做T买入交易所获收益 = 卖出金额 - (卖出份额 * 买入时净值)
            //BigDecimal sumAmount = fundShorttermTrading.getAmount().add(fundShorttermTradingMapper.selectSumAmountByRelatedTradingId(relatedTrading.getId()));
            //BigDecimal income = sumAmount.subtract(relatedTrading.getAmount());
            BigDecimal cost = param.getShares().multiply(relatedTrading.getNetValue());
            BigDecimal income = param.getAmount().subtract(cost);

            //总收益率 此处除的时候要保留4位小数，因为后面会乘以100
            BigDecimal totalCost = totalShares.multiply(relatedTrading.getNetValue());
            BigDecimal totalIncome = relatedTrading.getIncome().add(income);
            BigDecimal totalReturnRate = totalIncome.divide(totalCost, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));

            updateRelatedTrading.setIncome(totalIncome);
            updateRelatedTrading.setReturnRate(totalReturnRate);
            fundShorttermTradingMapper.updateByPrimaryKeySelective(updateRelatedTrading);


            //该笔卖出操作收益率 此处除的时候要保留4位小数，因为后面会乘以100
            BigDecimal returnRate = income.divide(cost, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));

            //修改收益 与 收益率字段
            param.setIncome(income);
            param.setReturnRate(returnRate);
        }

        //修改基金短期交易/做T交易记录
        param.setUpdateTime(new Date());
        fundShorttermTradingMapper.updateByPrimaryKeySelective(param);
    }

    @Override
    public void delete(FundShorttermTradingParam param) {
        FundShorttermTrading dbFundShorttermTrading = fundShorttermTradingMapper.selectById(param.getId());
        if(dbFundShorttermTrading == null){
            throw new StoneCustomerException("该基金短期交易/做T交易记录不存在，删除失败");
        }
        //删除基金短期交易/做T交易记录
        param.setUpdateTime(new Date());
        fundShorttermTradingMapper.deleteById(param.getId());
    }
}