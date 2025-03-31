package com.siam.system.modular.mod_finance.model.param;

import com.siam.system.modular.mod_finance.entity.CapitalRecord;
import lombok.Data;

import java.util.Date;

@Data
public class CapitalRecordParam extends CapitalRecord {

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

    //日期类型(0=日 1=月 2=年)
    private Integer dateType = 0;

    //排序字段(income=收入 expend=支出 amount=金额)
    private String sortKey;

    //排序方式(asc=从低到高 desc=从高到低)
    private String sortType = "desc";

    //展示方式(0=只展示收入大于0的数据 1=只展示支出大于0的数据)
    private Integer showType;
}