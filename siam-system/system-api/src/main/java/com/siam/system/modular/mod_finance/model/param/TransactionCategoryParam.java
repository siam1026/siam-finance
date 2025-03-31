package com.siam.system.modular.mod_finance.model.param;

import com.siam.system.modular.mod_finance.entity.TransactionCategory;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TransactionCategoryParam extends TransactionCategory {

    //开始日期
    private Date startCreateTime;

    //结束日期
    private Date endCreateTime;

    //页码
    private Integer pageNo = 1;

    //页面大小
    private Integer pageSize = 20;

    private List<Long> idList;
}