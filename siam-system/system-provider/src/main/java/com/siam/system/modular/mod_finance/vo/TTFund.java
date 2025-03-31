package com.siam.system.modular.mod_finance.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 天天基金实体对象类
 *
 */
@Data
public class TTFund {

    //基金代码
    private String fundcode;

    //基金名称
    private String name;

    //上一个交易日期
    private String jzrq;

    //上一个交易日净值
    private BigDecimal dwjz;

    //最新估算净值
    private BigDecimal gsz;

    //最新估算涨跌幅
    private BigDecimal gszzl;

    //最新交易日期
    private String gztime;

    //上一个交易日期
    private Date jzrqDate;

    //最新交易日期
    private Date gztimeDate;

}