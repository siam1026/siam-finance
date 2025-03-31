package com.siam.system.modular.mod_finance.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 新浪基金实体对象类
 *
 */
@Data
public class SinaStock {

    //股票名称
    private String name;

    //价格
    private BigDecimal price;

    //时间
    private Date date;

    //昨日价格
    private BigDecimal yesterdayPrice;

    //响应报文
    private String responseMessage;
}