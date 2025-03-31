package com.siam.system.modular.mod_finance.model.result;

import com.siam.system.modular.mod_finance.entity.GoldShorttermTrading;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class GoldShorttermTradingResult extends GoldShorttermTrading {

    //开始日期
    private Date startCreateTime;

    //结束日期
    private Date endCreateTime;

    //页码
    private Integer pageNo = 1;

    //页面大小
    private Integer pageSize = 20;

    //剩余克数
    private BigDecimal remainingGrams;

    //剩余克数比例
    private BigDecimal remainingGramsRate;

    //今日卖出可获收益
    private BigDecimal sellIncome;

    //今日卖出手续费
    private BigDecimal sellServiceCharge;

    //今日卖出收益率
    private BigDecimal sellReturnRate;
}