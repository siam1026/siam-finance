package com.siam.system.modular.mod_finance.model.result;

import com.siam.system.modular.mod_finance.entity.FundFixedInvestment;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FundFixedInvestmentResult extends FundFixedInvestment {

    //开始日期
    private Date startCreateTime;

    //结束日期
    private Date endCreateTime;

    //页码
    private Integer pageNo = 1;

    //页面大小
    private Integer pageSize = 20;

    //卖出记录关联的买入记录列表
    private List relatedTradingList;
}