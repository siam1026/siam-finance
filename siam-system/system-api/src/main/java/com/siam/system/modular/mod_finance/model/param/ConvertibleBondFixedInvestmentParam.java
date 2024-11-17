package com.siam.system.modular.mod_finance.model.param;

import com.siam.system.modular.mod_finance.entity.ConvertibleBondFixedInvestment;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 可转债定投交易记录表实例类
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
@Data
public class ConvertibleBondFixedInvestmentParam extends ConvertibleBondFixedInvestment {

    //开始日期
    private Date startCreateTime;

    //结束日期
    private Date endCreateTime;

    //页码
    private Integer pageNo = 1;

    //页面大小
    private Integer pageSize = 20;

    //关联的交易记录id列表
    private List<Integer> relatedTradingIdList;
}