package com.siam.system.modular.mod_finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.system.modular.mod_finance.entity.CapitalRecord;
import com.siam.system.modular.mod_finance.model.example.CapitalRecordExample;
import com.siam.system.modular.mod_finance.model.param.CapitalRecordParam;
import com.siam.system.modular.mod_finance.model.vo.CapitalStatisticsVo;

import java.util.List;
import java.util.Map;

public interface CapitalRecordService {
    int countByExample(CapitalRecordExample example);

    void deleteByPrimaryKey(Integer id);

    void insertSelective(CapitalRecord record);

    List<CapitalRecord> selectByExample(CapitalRecordExample example);

    CapitalRecord selectByPrimaryKey(Integer id);

    void updateByExampleSelective(CapitalRecord record, CapitalRecordExample example);

    void updateByPrimaryKeySelective(CapitalRecord record);

    Page getListByPage(CapitalRecordParam param);

//    BigDecimal selectSumAmountByRelatedRecordId(Integer relatedRecordId);
//
//    //计算各个资金分类的收入情况
//    BigDecimal selectIncomeByCapitalTypeIdAndCreateTime(int capitalTypeId, Date startDate, Date endDate);
//
//    //计算各个资金分类的支出情况
//    BigDecimal selectExpenditureByCapitalTypeIdAndCreateTime(int capitalTypeId, Date startDate, Date endDate);

    /**
     * 添加资金收入与支出记录
     *
     * @author JiangP
     */
    void insert(CapitalRecordParam param);

    /**
     * 修改资金收入与支出记录
     *
     * @author JiangP
     */
    void update(CapitalRecordParam param);

    /**
     * 统计资金收入与支出记录-柱状图
     *
     * @author JiangP
     */
    CapitalStatisticsVo statisticsOfHistogramChart(CapitalRecordParam param);

    /**
     * 统计资金收入与支出记录-饼图
     *
     * @author JiangP
     */
    CapitalStatisticsVo statisticsOfPieChart(CapitalRecordParam param);

    /**
     * 查询资金统计数据-折线图
     *
     * @author JiangP
     */
    Map statisticOfLineChart(CapitalRecordParam param);

    /**
     * 统计资金收入与支出金额
     *
     * @author JiangP
     */
    Map statistics(CapitalRecordParam param);

    /**
     * 统计总资产分布
     *
     * @author JiangP
     */
    Map totalCapitalStatistic(CapitalRecordParam param);
}