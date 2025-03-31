package com.siam.system.modular.mod_finance.model.param;

import com.siam.system.modular.mod_finance.entity.ConvertibleBondShorttermTrading;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 可转债短期交易/做T交易记录表(一个做T买入记录可能对应多个做T卖出操作)实例类
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
@Data
public class ConvertibleBondShorttermTradingParam extends ConvertibleBondShorttermTrading {

    //开始日期
    private Date startCreateTime;

    //结束日期
    private Date endCreateTime;

    //页码
    private Integer pageNo = 1;

    //页面大小
    private Integer pageSize = 20;

    //基金名称
    private String convertibleBondName;

    //剩余份额
    private BigDecimal remainingShares;

    //手续费
    private BigDecimal serviceCharge;

    //收益率
    private BigDecimal returnRate;
}