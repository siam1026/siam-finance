package com.siam.system.modular.mod_finance.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConvertibleBondStatisticsVo {

    //基金id
    private Integer convertibleBondId;

    //基金名称
    private String convertibleBondName;

    //基金金额
    private BigDecimal amount;

    //基金占比(%)
    private BigDecimal ratio;
}