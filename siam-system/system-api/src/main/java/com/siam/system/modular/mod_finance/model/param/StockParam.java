package com.siam.system.modular.mod_finance.model.param;

import com.siam.system.modular.mod_finance.entity.Stock;
import lombok.Data;

import java.util.Date;

/**
 * 股票信息表实例类
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
@Data
public class StockParam extends Stock {

    //开始日期
    private Date startCreateTime;

    //结束日期
    private Date endCreateTime;

    //页码
    private Integer pageNo = 1;

    //页面大小
    private Integer pageSize = 20;

    //开始日期
    private String startDate;

    //结束日期
    private String endDate;
}