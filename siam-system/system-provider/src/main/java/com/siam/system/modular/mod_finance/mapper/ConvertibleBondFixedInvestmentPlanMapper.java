package com.siam.system.modular.mod_finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.system.modular.mod_finance.entity.ConvertibleBondFixedInvestmentPlan;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * 可转债定投计划表数据层
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
public interface ConvertibleBondFixedInvestmentPlanMapper extends BaseMapper<ConvertibleBondFixedInvestmentPlan> {

    @ResultMap("BaseResultMap")
    @Select("<script>select ffip.* from tb_convertible_bond_fixed_investment_plan ffip" +
            "<where> 1=1 " +
            "<if test=\"convertibleBondFixedInvestmentPlan.id != null\"> AND ffip.id = #{convertibleBondFixedInvestmentPlan.id} </if>" +
            "<if test=\"convertibleBondFixedInvestmentPlan.memberId != null\"> AND ffip.member_id = #{convertibleBondFixedInvestmentPlan.memberId} </if>" +
            "<if test=\"convertibleBondFixedInvestmentPlan.convertibleBondIdComma != null and convertibleBondFixedInvestmentPlan.convertibleBondIdComma !=''\"> AND ffip.convertible_bond_id_comma like '%${convertibleBondFixedInvestmentPlan.convertibleBondIdComma}%' </if>" +
            "<if test=\"convertibleBondFixedInvestmentPlan.type != null\"> AND ffip.type = #{convertibleBondFixedInvestmentPlan.type} </if>" +
            "<if test=\"convertibleBondFixedInvestmentPlan.amount != null\"> AND ffip.amount = #{convertibleBondFixedInvestmentPlan.amount} </if>" +
            "<if test=\"convertibleBondFixedInvestmentPlan.cycle != null\"> AND ffip.cycle = #{convertibleBondFixedInvestmentPlan.cycle} </if>" +
            "<if test=\"convertibleBondFixedInvestmentPlan.expectedReturnRate != null\"> AND ffip.expected_return_rate = #{convertibleBondFixedInvestmentPlan.expectedReturnRate} </if>" +
            "<if test=\"convertibleBondFixedInvestmentPlan.status != null and convertibleBondFixedInvestmentPlan.status != -1\"> AND ffip.status = #{convertibleBondFixedInvestmentPlan.status} </if>" +
            "<if test=\"convertibleBondFixedInvestmentPlan.status == -1\"> AND (ffip.status = 0 or ffip.status = 1 or ffip.status = 2) </if>" +
            "<if test=\"convertibleBondFixedInvestmentPlan.actualReturnRate != null\"> AND ffip.actual_return_rate = #{convertibleBondFixedInvestmentPlan.actualReturnRate} </if>" +
            "<if test=\"convertibleBondFixedInvestmentPlan.income != null\"> AND ffip.income = #{convertibleBondFixedInvestmentPlan.income} </if>" +
            "<if test=\"convertibleBondFixedInvestmentPlan.remarks != null and convertibleBondFixedInvestmentPlan.remarks !=''\"> AND ffip.remarks like '%${convertibleBondFixedInvestmentPlan.remarks}%' </if>" +
            "</where> order by ffip.id asc" +
            "</script>")
    Page<ConvertibleBondFixedInvestmentPlan> getListByPage(@Param("page") Page page, @Param("convertibleBondFixedInvestmentPlan") ConvertibleBondFixedInvestmentPlan convertibleBondFixedInvestmentPlan);

    @ResultMap("CustomResultMap")
    @Select("<script>select ffip.* from tb_convertible_bond_fixed_investment_plan ffip" +
            "<where> 1=1 " +
            "<if test=\"convertibleBondFixedInvestmentPlan.id != null\"> AND ffip.id = #{convertibleBondFixedInvestmentPlan.id} </if>" +
            "<if test=\"convertibleBondFixedInvestmentPlan.memberId != null\"> AND ffip.member_id = #{convertibleBondFixedInvestmentPlan.memberId} </if>" +
            "<if test=\"convertibleBondFixedInvestmentPlan.convertibleBondIdComma != null and convertibleBondFixedInvestmentPlan.convertibleBondIdComma !=''\"> AND ffip.convertible_bond_id_comma like '%${convertibleBondFixedInvestmentPlan.convertibleBondIdComma}%' </if>" +
            "<if test=\"convertibleBondFixedInvestmentPlan.type != null\"> AND ffip.type = #{convertibleBondFixedInvestmentPlan.type} </if>" +
            "<if test=\"convertibleBondFixedInvestmentPlan.amount != null\"> AND ffip.amount = #{convertibleBondFixedInvestmentPlan.amount} </if>" +
            "<if test=\"convertibleBondFixedInvestmentPlan.cycle != null\"> AND ffip.cycle = #{convertibleBondFixedInvestmentPlan.cycle} </if>" +
            "<if test=\"convertibleBondFixedInvestmentPlan.expectedReturnRate != null\"> AND ffip.expected_return_rate = #{convertibleBondFixedInvestmentPlan.expectedReturnRate} </if>" +
            "<if test=\"convertibleBondFixedInvestmentPlan.status != null and convertibleBondFixedInvestmentPlan.status != -1\"> AND ffip.status = #{convertibleBondFixedInvestmentPlan.status} </if>" +
            "<if test=\"convertibleBondFixedInvestmentPlan.status == -1\"> AND (ffip.status = 0 or ffip.status = 1 or ffip.status = 2) </if>" +
            "<if test=\"convertibleBondFixedInvestmentPlan.actualReturnRate != null\"> AND ffip.actual_return_rate = #{convertibleBondFixedInvestmentPlan.actualReturnRate} </if>" +
            "<if test=\"convertibleBondFixedInvestmentPlan.income != null\"> AND ffip.income = #{convertibleBondFixedInvestmentPlan.income} </if>" +
            "<if test=\"convertibleBondFixedInvestmentPlan.remarks != null and convertibleBondFixedInvestmentPlan.remarks !=''\"> AND ffip.remarks like '%${convertibleBondFixedInvestmentPlan.remarks}%' </if>" +
            "</where> order by ffip.id asc" +
            "</script>")
    Page<Map<String, Object>> getMapListByPage(@Param("page") Page page, @Param("convertibleBondFixedInvestmentPlan") ConvertibleBondFixedInvestmentPlan convertibleBondFixedInvestmentPlan);
}