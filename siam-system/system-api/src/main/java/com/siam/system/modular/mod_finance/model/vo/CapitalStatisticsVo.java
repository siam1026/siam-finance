package com.siam.system.modular.mod_finance.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class CapitalStatisticsVo {

//    //计算各个资金分类的收入情况
//    private BigDecimal income_baodi;
//    private BigDecimal income_fanbu;
//    private BigDecimal income_chuxu;
//    private BigDecimal income_yeyu;
//    private BigDecimal income_boyi;
//    private BigDecimal income_boyi_b;
//    private BigDecimal income_total;
//
//    //计算各个资金分类的支出情况
//    private BigDecimal expenditure_baodi;
//    private BigDecimal expenditure_fanbu;
//    private BigDecimal expenditure_chuxu;
//    private BigDecimal expenditure_yeyu;
//    private BigDecimal expenditure_boyi;
//    private BigDecimal expenditure_boyi_b;
//    private BigDecimal expenditure_total;
//
//    //计算各个资金分类的现存资金情况
//    private BigDecimal leave_baodi;
//    private BigDecimal leave_fanbu;
//    private BigDecimal leave_chuxu;
//    private BigDecimal leave_yeyu;
//    private BigDecimal leave_boyi;
//    private BigDecimal leave_boyi_b;
//    private BigDecimal leave_total;
//
//    //计算可用资金
//    private BigDecimal available_total;


    //统计数据
    List<Map<String, Object>> list;

    //总金额
    private BigDecimal totalAmount;

    //总收入
    private BigDecimal totalIncome;

    //总支出
    private BigDecimal totalExpend;

    //总结余
    private BigDecimal totalLeave;
}