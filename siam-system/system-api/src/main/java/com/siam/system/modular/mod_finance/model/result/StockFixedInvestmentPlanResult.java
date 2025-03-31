package com.siam.system.modular.mod_finance.model.result;

import com.siam.system.modular.mod_finance.entity.StockFixedInvestmentPlan;
import lombok.Data;

import java.util.Date;

/**
 * 股票定投计划表实例类
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
@Data
public class StockFixedInvestmentPlanResult extends StockFixedInvestmentPlan {

    //开始日期
    private Date startCreateTime;

    //结束日期
    private Date endCreateTime;

    //页码
    private Integer pageNo = 1;

    //页面大小
    private Integer pageSize = 20;
}