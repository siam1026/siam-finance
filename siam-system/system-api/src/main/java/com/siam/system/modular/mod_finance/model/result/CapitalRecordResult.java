package com.siam.system.modular.mod_finance.model.result;

import com.siam.system.modular.mod_finance.entity.CapitalRecord;
import lombok.Data;

import java.util.Date;

@Data
public class CapitalRecordResult extends CapitalRecord {

    //开始日期
    private Date startCreateTime;

    //结束日期
    private Date endCreateTime;

    //页码
    private Integer pageNo = 1;

    //页面大小
    private Integer pageSize = 20;

    //交易分类名称
    private String transactionCategoryName;
}