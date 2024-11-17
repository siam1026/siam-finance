package com.siam.system.modular.mod_finance.controller.member;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.constant.BaseCode;
import com.siam.package_common.constant.Quantity;
import com.siam.system.modular.mod_finance.entity.Gold;
import com.siam.system.modular.mod_finance.entity.GoldShorttermTrading;
import com.siam.system.modular.mod_finance.model.param.GoldShorttermTradingParam;
import com.siam.system.modular.mod_finance.model.result.GoldShorttermTradingResult;
import com.siam.system.modular.mod_finance.service.GoldService;
import com.siam.system.modular.mod_finance.service.GoldShorttermTradingService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;

@RestController
@RequestMapping(value = "/rest/member/goldShorttermTrading")
@Transactional(rollbackFor = Exception.class)
@Api(tags = "黄金短期交易/做T交易记录模块相关接口", description = "GoldShorttermTradingController")
public class GoldShorttermTradingController {

    @Autowired
    private GoldShorttermTradingService goldShorttermTradingService;

    @Autowired
    private GoldService goldService;

    /**
     * 黄金短期交易/做T交易记录列表
     *
     * @author JiangP
     */
    @PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) GoldShorttermTradingParam param) {
        BasicData basicResult = new BasicData();

        Page<GoldShorttermTradingResult> page = goldShorttermTradingService.getListByPage(param.getPageNo(), param.getPageSize(), param);

        //对进行中的做T交易买入记录进行分析
        if(param.getType()!=null && param.getStatus()!=null && param.getType()==Quantity.INT_0 && param.getStatus()==Quantity.INT_1){
            //卖出手续费比例暂时按照0.5%来算
            BigDecimal serviceChargeRatio = BigDecimal.valueOf(0.5);

            //计算剩余份额、当日做T卖出可获收益(已扣除手续费),手续费,收益率
            page.getRecords().forEach(result -> {
                Gold dbGold = goldService.selectByPrimaryKey(result.getGoldId());

                //剩余份额
                BigDecimal remainingGrams = result.getGrams().subtract(goldShorttermTradingService.selectSumSellSharesByRelatedTradingId(result.getGoldId()));

                //剩余份额百分比
                BigDecimal remainingGramsRate = remainingGrams.divide((BigDecimal) result.getGrams(), 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));

                //计算规则：收益 = (基金最新净值 * 做T买入份额) - 手续费 - 做T买入金额
                BigDecimal sellIncome = dbGold.getLastestPrice().multiply(((BigDecimal) result.getGrams())).setScale(2, BigDecimal.ROUND_HALF_UP);
                //手续费  注意手续费比例要除以100
                BigDecimal sellServiceCharge = sellIncome.multiply(serviceChargeRatio.divide(BigDecimal.valueOf(100))).setScale(2, BigDecimal.ROUND_HALF_UP);
                sellIncome = sellIncome.subtract(sellServiceCharge).subtract(((BigDecimal) result.getAmount()));

                //收益率 此处除的时候要保留4位小数，因为后面会乘以100
                BigDecimal sellReturnRate = sellIncome.divide(((BigDecimal) result.getAmount()), 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));

                result.setRemainingGrams(remainingGrams);
                result.setRemainingGramsRate(remainingGramsRate);
                result.setSellIncome(sellIncome);
                result.setSellServiceCharge(sellServiceCharge);
                result.setSellReturnRate(sellReturnRate);
            });
        }

        basicResult.setData(page);
        basicResult.setSuccess(true);
        basicResult.setCode(BaseCode.SUCCESS);
        basicResult.setMessage("查询成功");
        return basicResult;
    }

    /**
     * 添加黄金短期交易/做T交易记录
     *
     * @author JiangP
     */
    @PostMapping(value = "/insert")
    public BasicResult insertGoldShorttermTrading(@RequestBody @Validated(value = {}) GoldShorttermTradingParam param) {
        BasicResult basicResult = new BasicResult();

        //如果是卖出操作，则需要计算 做T卖出操作所获收益，并且改变 做T状态
        if(param.getType() == Quantity.INT_1){
            //找到做T卖出操作关联的做T买入交易id
            GoldShorttermTrading relatedGoldShorttermTrading = goldShorttermTradingService.selectByPrimaryKey(param.getRelatedTradingId());
            if(relatedGoldShorttermTrading == null){
                basicResult.setSuccess(false);
                basicResult.setCode(BaseCode.ERR);
                basicResult.setMessage("关联的做T买入交易记录不存在，添加失败");
                return basicResult;
            }else if(relatedGoldShorttermTrading.getStatus() == Quantity.INT_2){
                basicResult.setSuccess(false);
                basicResult.setCode(BaseCode.ERR);
                basicResult.setMessage("关联的做T买入交易记录处于已完成状态，添加失败");
                return basicResult;
            }

            //查询该关联的做T买入交易记录的所有卖出份额 是否与 买入份额相等，如果相等则代表该笔做T买入交易已完成
            BigDecimal grams = param.getGrams().add(goldShorttermTradingService.selectSumGramsByRelatedTradingId(relatedGoldShorttermTrading.getId()));
            if(grams.compareTo(relatedGoldShorttermTrading.getGrams()) == 0){
                //计算该笔做T买入交易所获收益
                BigDecimal sumAmount = param.getAmount().add(goldShorttermTradingService.selectSumAmountByRelatedTradingId(relatedGoldShorttermTrading.getId()));
                BigDecimal income = sumAmount.subtract(relatedGoldShorttermTrading.getAmount());

                //该笔卖出操作收益率 此处除的时候要保留4位小数，因为后面会乘以100
                BigDecimal returnRate = income.divide(relatedGoldShorttermTrading.getAmount(), 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));

                //修改关联的做T买入交易记录，将做T状态改为已完成
                GoldShorttermTrading updateGoldShorttermTrading = new GoldShorttermTrading();
                updateGoldShorttermTrading.setId(relatedGoldShorttermTrading.getId());
                updateGoldShorttermTrading.setStatus(Quantity.INT_2);
                updateGoldShorttermTrading.setIncome(income);
                updateGoldShorttermTrading.setReturnRate(returnRate);

                goldShorttermTradingService.updateByPrimaryKeySelective(updateGoldShorttermTrading);
            }

            //1、计算单笔卖出所获收益/收益率
            //该笔做T买入交易所获收益 = 卖出金额 - (卖出份额 * 买入时净值)
            BigDecimal cost = param.getGrams().multiply(relatedGoldShorttermTrading.getPrice());
            BigDecimal income = param.getAmount().subtract(cost);

            //该笔卖出操作收益率 此处除的时候要保留4位小数，因为后面会乘以100
            BigDecimal returnRate = income.divide(cost, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));

            //关联写入黄金id，卖出操作的状态要设置为无
            param.setGoldId(relatedGoldShorttermTrading.getGoldId());
            param.setStatus(Quantity.INT_0);
            param.setIncome(income);
            param.setReturnRate(returnRate);
        }else{
            //买入操作的状态要设置为进行中
            param.setStatus(Quantity.INT_1);
        }

        //添加黄金短期交易/做T交易记录
        param.setCreateTime(new Date());
        param.setUpdateTime(new Date());
        goldShorttermTradingService.insertSelective(param);

        basicResult.setSuccess(true);
        basicResult.setCode(BaseCode.SUCCESS);
        basicResult.setMessage("添加成功");
        return basicResult;
    }

    /**
     * 修改黄金短期交易/做T交易记录
     *
     * @author JiangP
     */
    @PostMapping(value = "/update")
    public BasicResult updateGoldShorttermTrading(@RequestBody @Validated(value = {}) GoldShorttermTradingParam param) {
        BasicResult basicResult = new BasicResult();

        GoldShorttermTrading dbGoldShorttermTrading = goldShorttermTradingService.selectByPrimaryKey(param.getId());
        if(dbGoldShorttermTrading == null){
            basicResult.setSuccess(false);
            basicResult.setCode(BaseCode.ERR);
            basicResult.setMessage("该黄金短期交易/做T交易记录不存在，修改失败");
            return basicResult;
        }

        //修改黄金短期交易/做T交易记录
        param.setUpdateTime(new Date());
        goldShorttermTradingService.updateByPrimaryKeySelective(param);

        basicResult.setSuccess(true);
        basicResult.setCode(BaseCode.SUCCESS);
        basicResult.setMessage("修改成功");
        return basicResult;
    }

    /**
     * 删除黄金短期交易/做T交易记录
     *
     * @author JiangP
     */
    @PostMapping(value = "/delete")
    public BasicResult deleteGoldShorttermTrading(@RequestBody @Validated(value = {}) GoldShorttermTradingParam param) {
        BasicResult basicResult = new BasicResult();

        GoldShorttermTrading dbGoldShorttermTrading = goldShorttermTradingService.selectByPrimaryKey(param.getId());
        if(dbGoldShorttermTrading == null){
            basicResult.setSuccess(false);
            basicResult.setCode(BaseCode.ERR);
            basicResult.setMessage("该黄金短期交易/做T交易记录不存在，删除失败");
            return basicResult;
        }

        //删除黄金短期交易/做T交易记录
        param.setUpdateTime(new Date());
        goldShorttermTradingService.deleteByPrimaryKey(param.getId());

        basicResult.setSuccess(true);
        basicResult.setCode(BaseCode.SUCCESS);
        basicResult.setMessage("修改成功");
        return basicResult;
    }
}