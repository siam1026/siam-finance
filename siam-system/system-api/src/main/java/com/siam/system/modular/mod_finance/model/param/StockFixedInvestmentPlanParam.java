package com.siam.system.modular.mod_finance.model.param;

import com.siam.system.modular.mod_finance.entity.StockFixedInvestmentPlan;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 股票定投计划表实例类
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
@Data
public class StockFixedInvestmentPlanParam extends StockFixedInvestmentPlan {

    //开始日期
    private Date startCreateTime;

    //结束日期
    private Date endCreateTime;

    //页码
    private Integer pageNo = 1;

    //页面大小
    private Integer pageSize = 20;

    //基金名称
    private String stockName;

    //剩余份额
    private BigDecimal remainingShares;

    //手续费
    private BigDecimal serviceCharge;

    //收益率
    private BigDecimal returnRate;
}