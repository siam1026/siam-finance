package com.siam.system.modular.mod_finance.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockStatisticsVo {

    //基金id
    private Integer stockId;

    //基金名称
    private String stockName;

    //基金金额
    private BigDecimal amount;

    //基金占比(%)
    private BigDecimal ratio;
}