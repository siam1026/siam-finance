package com.siam.system.modular.mod_finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.system.modular.mod_finance.entity.StockFixedInvestmentPlan;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * 股票定投计划表数据层
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
public interface StockFixedInvestmentPlanMapper extends BaseMapper<StockFixedInvestmentPlan> {

    @ResultMap("BaseResultMap")
    @Select("<script>select ffip.* from tb_stock_fixed_investment_plan ffip" +
            "<where> 1=1 " +
            "<if test=\"stockFixedInvestmentPlan.id != null\"> AND ffip.id = #{stockFixedInvestmentPlan.id} </if>" +
            "<if test=\"stockFixedInvestmentPlan.memberId != null\"> AND ffip.member_id = #{stockFixedInvestmentPlan.memberId} </if>" +
            "<if test=\"stockFixedInvestmentPlan.stockIdComma != null and stockFixedInvestmentPlan.stockIdComma !=''\"> AND ffip.stock_id_comma like '%${stockFixedInvestmentPlan.stockIdComma}%' </if>" +
            "<if test=\"stockFixedInvestmentPlan.type != null\"> AND ffip.type = #{stockFixedInvestmentPlan.type} </if>" +
            "<if test=\"stockFixedInvestmentPlan.amount != null\"> AND ffip.amount = #{stockFixedInvestmentPlan.amount} </if>" +
            "<if test=\"stockFixedInvestmentPlan.cycle != null\"> AND ffip.cycle = #{stockFixedInvestmentPlan.cycle} </if>" +
            "<if test=\"stockFixedInvestmentPlan.expectedReturnRate != null\"> AND ffip.expected_return_rate = #{stockFixedInvestmentPlan.expectedReturnRate} </if>" +
            "<if test=\"stockFixedInvestmentPlan.status != null and stockFixedInvestmentPlan.status != -1\"> AND ffip.status = #{stockFixedInvestmentPlan.status} </if>" +
            "<if test=\"stockFixedInvestmentPlan.status == -1\"> AND (ffip.status = 0 or ffip.status = 1 or ffip.status = 2) </if>" +
            "<if test=\"stockFixedInvestmentPlan.actualReturnRate != null\"> AND ffip.actual_return_rate = #{stockFixedInvestmentPlan.actualReturnRate} </if>" +
            "<if test=\"stockFixedInvestmentPlan.income != null\"> AND ffip.income = #{stockFixedInvestmentPlan.income} </if>" +
            "<if test=\"stockFixedInvestmentPlan.remarks != null and stockFixedInvestmentPlan.remarks !=''\"> AND ffip.remarks like '%${stockFixedInvestmentPlan.remarks}%' </if>" +
            "</where> order by ffip.id asc" +
            "</script>")
    Page<StockFixedInvestmentPlan> getListByPage(@Param("page") Page page, @Param("stockFixedInvestmentPlan") StockFixedInvestmentPlan stockFixedInvestmentPlan);

    @ResultMap("CustomResultMap")
    @Select("<script>select ffip.* from tb_stock_fixed_investment_plan ffip" +
            "<where> 1=1 " +
            "<if test=\"stockFixedInvestmentPlan.id != null\"> AND ffip.id = #{stockFixedInvestmentPlan.id} </if>" +
            "<if test=\"stockFixedInvestmentPlan.memberId != null\"> AND ffip.member_id = #{stockFixedInvestmentPlan.memberId} </if>" +
            "<if test=\"stockFixedInvestmentPlan.stockIdComma != null and stockFixedInvestmentPlan.stockIdComma !=''\"> AND ffip.stock_id_comma like '%${stockFixedInvestmentPlan.stockIdComma}%' </if>" +
            "<if test=\"stockFixedInvestmentPlan.type != null\"> AND ffip.type = #{stockFixedInvestmentPlan.type} </if>" +
            "<if test=\"stockFixedInvestmentPlan.amount != null\"> AND ffip.amount = #{stockFixedInvestmentPlan.amount} </if>" +
            "<if test=\"stockFixedInvestmentPlan.cycle != null\"> AND ffip.cycle = #{stockFixedInvestmentPlan.cycle} </if>" +
            "<if test=\"stockFixedInvestmentPlan.expectedReturnRate != null\"> AND ffip.expected_return_rate = #{stockFixedInvestmentPlan.expectedReturnRate} </if>" +
            "<if test=\"stockFixedInvestmentPlan.status != null and stockFixedInvestmentPlan.status != -1\"> AND ffip.status = #{stockFixedInvestmentPlan.status} </if>" +
            "<if test=\"stockFixedInvestmentPlan.status == -1\"> AND (ffip.status = 0 or ffip.status = 1 or ffip.status = 2) </if>" +
            "<if test=\"stockFixedInvestmentPlan.actualReturnRate != null\"> AND ffip.actual_return_rate = #{stockFixedInvestmentPlan.actualReturnRate} </if>" +
            "<if test=\"stockFixedInvestmentPlan.income != null\"> AND ffip.income = #{stockFixedInvestmentPlan.income} </if>" +
            "<if test=\"stockFixedInvestmentPlan.remarks != null and stockFixedInvestmentPlan.remarks !=''\"> AND ffip.remarks like '%${stockFixedInvestmentPlan.remarks}%' </if>" +
            "</where> order by ffip.id asc" +
            "</script>")
    Page<Map<String, Object>> getMapListByPage(@Param("page") Page page, @Param("stockFixedInvestmentPlan") StockFixedInvestmentPlan stockFixedInvestmentPlan);
}