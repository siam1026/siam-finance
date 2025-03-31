package com.siam.system.modular.mod_finance.service_impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.siam.package_common.constant.Quantity;
import com.siam.package_common.exception.StoneCustomerException;
import com.siam.system.modular.mod_finance.entity.Stock;
import com.siam.system.modular.mod_finance.entity.StockShorttermTrading;
import com.siam.system.modular.mod_finance.mapper.StockShorttermTradingMapper;
import com.siam.system.modular.mod_finance.model.param.StockShorttermTradingParam;
import com.siam.system.modular.mod_finance.service.StockService;
import com.siam.system.modular.mod_finance.service.StockShorttermTradingService;
import com.siam.system.modular.mod_user.auth.cache.MemberSessionManager;
import com.siam.system.modular.mod_user.entity.Member;
import com.siam.system.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 股票短期交易/做T交易记录表(一个做T买入记录可能对应多个做T卖出操作)业务实现层
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
@Service
public class StockShorttermTradingServiceImpl extends ServiceImpl<StockShorttermTradingMapper, StockShorttermTrading> implements StockShorttermTradingService {

    @Autowired
    private StockShorttermTradingMapper stockShorttermTradingMapper;

    @Autowired
    private StockService stockService;

    @Autowired
    private MemberSessionManager memberSessionManager;

    @Override
    public Page<StockShorttermTrading> getListByPage(int pageNo, int pageSize, StockShorttermTrading stockShorttermTrading) {
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        stockShorttermTrading.setMemberId(loginMember.getId());
        Page<StockShorttermTrading> page = stockShorttermTradingMapper.getListByPage(new Page(pageNo, pageSize), stockShorttermTrading);
        return page;
    }

    public Page<Map<String, Object>> getListByPageJoinStock(int pageNo, int pageSize, StockShorttermTradingParam param){
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        param.setMemberId(loginMember.getId());
        Page<Map<String, Object>> page = stockShorttermTradingMapper.getListByPageJoinStock(new Page(param.getPageNo(), param.getPageSize()), param);

        //对进行中的做T交易买入记录进行分析
        if(param.getType()!=null && param.getStatus()!=null && param.getType()== Quantity.INT_0 && param.getStatus()==Quantity.INT_1){
            //卖出手续费比例暂时按照0.5%来算
            BigDecimal serviceChargeRatio = BigDecimal.valueOf(0.5);

            //计算剩余份额、当日做T卖出可获收益(已扣除手续费),手续费,收益率
            page.getRecords().forEach(map -> {
                Stock dbStock = stockService.getById((int) map.get("stockId"));

                //剩余份额
                BigDecimal remainingShares = ((BigDecimal) map.get("quantity")).subtract(stockShorttermTradingMapper.selectSumSellSharesByRelatedTradingId((int) map.get("id")));

                //剩余份额百分比
                BigDecimal remainingSharesRate = remainingShares.divide((BigDecimal) map.get("quantity"), 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));

                //计算规则：收益 = (基金最新净值 * 做T买入份额) - 手续费 - 做T买入金额
                BigDecimal sellIncome = dbStock.getLastestPrice().multiply(((BigDecimal) map.get("quantity"))).setScale(2, BigDecimal.ROUND_HALF_UP);
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
    /*public Page<Map<String, Object>> getListByPageJoinStock(int pageNo, int pageSize, StockShorttermTradingParam param){
        Page<Map<String, Object>> page = stockShorttermTradingMapper.getListByPageJoinStock(new Page(param.getPageNo(), param.getPageSize()), param);

        //对进行中的做T交易买入记录进行分析
        if(param.getType()!=null && param.getStatus()!=null && param.getType()==Quantity.INT_0 && param.getStatus()==Quantity.INT_1){
            //卖出手续费比例暂时按照0.5%来算
            BigDecimal serviceChargeRatio = BigDecimal.valueOf(0.5);

            //计算剩余份额、当日做T卖出可获收益(已扣除手续费),手续费,收益率
            for (int i = 0; i < page.getRecords().size(); i++) {
                StockShorttermTrading dbStockShorttermTrading = page.getRecords().get(i);

                Stock dbStock = stockService.detail(dbStockShorttermTrading.getStockId());

                //剩余份额
                BigDecimal remainingShares = dbStockShorttermTrading.getShares().subtract(stockShorttermTradingMapper.selectSumSellSharesByRelatedTradingId(dbStockShorttermTrading.getRelatedTradingId()));

                //计算规则：收益 = (基金最新净值 * 做T买入份额) - 手续费 - 做T买入金额
                BigDecimal income = dbStock.getLastestPrice().multiply(dbStockShorttermTrading.getShares()).setScale(2, BigDecimal.ROUND_HALF_UP);
                //手续费  注意手续费比例要除以100
                BigDecimal serviceCharge = income.multiply(serviceChargeRatio.divide(BigDecimal.valueOf(100))).setScale(2, BigDecimal.ROUND_HALF_UP);
                income = income.subtract(serviceCharge).subtract(dbStockShorttermTrading.getAmount());

                //收益率
                BigDecimal returnRate = income.divide(dbStockShorttermTrading.getAmount(), 2, BigDecimal.ROUND_HALF_UP);

                dbStockShorttermTrading.setIncome(income);

                StockShorttermTradingDto stockShorttermTradingDto = new StockShorttermTradingDto();
                stockShorttermTradingDto.setRemainingShares(remainingShares);
                stockShorttermTradingDto.setServiceCharge(serviceCharge);
                stockShorttermTradingDto.setReturnRate(returnRate);
                BeanUtils.copyProperties(dbStockShorttermTrading, stockShorttermTradingDto);

                //替换list集合中的元素  注意不是add()--是set()
                page.getRecords().set(i, stockShorttermTradingDto);
            }
        }
        return page;
    }*/

    @Override
    public BigDecimal selectSumSellSharesByRelatedTradingId(Integer relatedTradingId) {
        return stockShorttermTradingMapper.selectSumSellSharesByRelatedTradingId(relatedTradingId);
    }

    @Override
    public BigDecimal selectSumSellAmountByRelatedTradingId(Integer relatedTradingId) {
        return stockShorttermTradingMapper.selectSumSellAmountByRelatedTradingId(relatedTradingId);
    }

    @Override
    public void insert(StockShorttermTradingParam param) {
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        param.setMemberId(loginMember.getId());

        //如果是卖出操作，则需要计算 做T卖出操作所获收益，并且改变 做T状态
        if(param.getType() == Quantity.INT_1){
            //找到做T卖出操作关联的做T买入交易id
            StockShorttermTrading relatedTrading = stockShorttermTradingMapper.selectById(param.getRelatedTradingId());
            if(relatedTrading == null){
                throw new StoneCustomerException("关联的做T买入交易记录不存在，添加失败");
            }else if(relatedTrading.getStatus() == Quantity.INT_2){
                throw new StoneCustomerException("关联的做T买入交易记录处于已完成状态，添加失败");
            }
            //1、计算单笔卖出所获收益/收益率
            //该笔做T买入交易所获收益 = 卖出金额 - (卖出份额 * 买入时净值)
            //BigDecimal sumAmount = stockShorttermTrading.getAmount().add(stockShorttermTradingMapper.selectSumAmountByRelatedTradingId(relatedTrading.getId()));
            //BigDecimal income = sumAmount.subtract(relatedTrading.getAmount());
            BigDecimal cost = param.getQuantity().multiply(relatedTrading.getPrice());
            BigDecimal income = param.getAmount().subtract(cost);

            //该笔卖出操作收益率 此处除的时候要保留4位小数，因为后面会乘以100
            BigDecimal returnRate = income.divide(cost, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));

            //关联写入基金id，卖出操作的状态要设置为无
            param.setStockId(relatedTrading.getStockId());
            param.setStatus(Quantity.INT_0);
            param.setIncome(income);
            param.setReturnRate(returnRate);
            //2 添加基金短期交易/做T交易记录
            //stockShorttermTrading.setCreateTime(new Date());
            param.setUpdateTime(new Date());
            stockShorttermTradingMapper.insert(param);

            //2、计算总做T记录卖出所获收益/收益率
            //当该笔做T记录完成时才进行总收益/总收益率计算，计算时需要减去买入手续费
            //查询该关联的做T买入交易记录的所有卖出份额 是否与 买入份额相等，如果相等则代表该笔做T买入交易已完成
            BigDecimal sumSellShares = stockShorttermTradingMapper.selectSumSellSharesByRelatedTradingId(relatedTrading.getId());
            if(sumSellShares.compareTo(relatedTrading.getQuantity()) == 0){
                //总收益率 此处除的时候要保留4位小数，因为后面会乘以100
                //当该笔做T记录完成时才进行总收益/总收益率计算，计算时需要减去买入手续费
                //总收益不能用份额*净值来计算，要以实际买入金额-累计实际卖出金额来计算
                /*BigDecimal totalCost = totalShares.multiply(relatedTrading.getPrice());
                BigDecimal totalIncome = relatedTrading.getIncome().add(income);
                BigDecimal totalReturnRate = totalIncome.divide(totalCost, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));*/

                BigDecimal sumSellAmount = stockShorttermTradingMapper.selectSumSellAmountByRelatedTradingId(relatedTrading.getId());
                BigDecimal totalIncome = sumSellAmount.subtract(relatedTrading.getAmount());
                BigDecimal totalReturnRate = totalIncome.divide(relatedTrading.getAmount(), 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));

                //将做T状态改为已完成
                //修改关联的做T买入交易记录
                StockShorttermTrading updateRelatedTrading = new StockShorttermTrading();
                updateRelatedTrading.setId(relatedTrading.getId());
                updateRelatedTrading.setStatus(Quantity.INT_2);
                updateRelatedTrading.setIncome(totalIncome);
                updateRelatedTrading.setReturnRate(totalReturnRate);
                stockShorttermTradingMapper.updateById(updateRelatedTrading);
            }

        }else{
            //买入操作的状态要设置为进行中
            param.setStatus(Quantity.INT_1);
            //2 添加基金短期交易/做T交易记录
            //stockShorttermTrading.setCreateTime(new Date());
            param.setUpdateTime(new Date());
            stockShorttermTradingMapper.insert(param);
        }
    }

    @Override
    public void update(StockShorttermTradingParam param) {

        StockShorttermTrading dbStockShorttermTrading = stockShorttermTradingMapper.selectById(param.getId());
        if(dbStockShorttermTrading == null){
            throw new StoneCustomerException("该基金短期交易/做T交易记录不存在，修改失败");
        }

        //如果是卖出操作，则需要重新计算 做T卖出操作所获收益
        if(dbStockShorttermTrading.getType() == Quantity.INT_1){
            //找到做T卖出操作关联的做T买入交易id
            StockShorttermTrading relatedTrading = stockShorttermTradingMapper.selectById(dbStockShorttermTrading.getRelatedTradingId());
            if(relatedTrading == null){
                throw new StoneCustomerException("关联的做T买入交易记录不存在，修改失败");
            }else if(relatedTrading.getStatus() == Quantity.INT_2){
                throw new StoneCustomerException("关联的做T买入交易记录处于已完成状态，修改失败");
            }

            //1 修改关联的做T买入交易记录
            StockShorttermTrading updateRelatedTrading = new StockShorttermTrading();
            updateRelatedTrading.setId(relatedTrading.getId());

            //查询该关联的做T买入交易记录的所有卖出份额 是否与 买入份额相等，如果相等则代表该笔做T买入交易已完成
            BigDecimal totalShares = param.getQuantity().add(stockShorttermTradingMapper.selectSumSellSharesByRelatedTradingId(relatedTrading.getId())).subtract(dbStockShorttermTrading.getQuantity());
            if(totalShares.compareTo(relatedTrading.getQuantity()) == 0){
                //将做T状态改为已完成
                updateRelatedTrading.setStatus(Quantity.INT_2);
            }

            //该笔做T买入交易所获收益 = 卖出金额 - (卖出份额 * 买入时净值)
            //BigDecimal sumAmount = stockShorttermTrading.getAmount().add(stockShorttermTradingMapper.selectSumAmountByRelatedTradingId(relatedTrading.getId()));
            //BigDecimal income = sumAmount.subtract(relatedTrading.getAmount());
            BigDecimal cost = param.getQuantity().multiply(relatedTrading.getPrice());
            BigDecimal income = param.getAmount().subtract(cost);

            //总收益率 此处除的时候要保留4位小数，因为后面会乘以100
            BigDecimal totalCost = totalShares.multiply(relatedTrading.getPrice());
            BigDecimal totalIncome = relatedTrading.getIncome().add(income);
            BigDecimal totalReturnRate = totalIncome.divide(totalCost, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));

            updateRelatedTrading.setIncome(totalIncome);
            updateRelatedTrading.setReturnRate(totalReturnRate);
            stockShorttermTradingMapper.updateById(updateRelatedTrading);


            //该笔卖出操作收益率 此处除的时候要保留4位小数，因为后面会乘以100
            BigDecimal returnRate = income.divide(cost, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));

            //修改收益 与 收益率字段
            param.setIncome(income);
            param.setReturnRate(returnRate);
        }

        //修改基金短期交易/做T交易记录
        param.setUpdateTime(new Date());
        stockShorttermTradingMapper.updateById(param);
    }

    @Override
    public void delete(StockShorttermTradingParam param) {
        StockShorttermTrading dbStockShorttermTrading = stockShorttermTradingMapper.selectById(param.getId());
        if(dbStockShorttermTrading == null){
            throw new StoneCustomerException("该基金短期交易/做T交易记录不存在，删除失败");
        }
        //删除基金短期交易/做T交易记录
        param.setUpdateTime(new Date());
        stockShorttermTradingMapper.deleteById(param.getId());
    }
}