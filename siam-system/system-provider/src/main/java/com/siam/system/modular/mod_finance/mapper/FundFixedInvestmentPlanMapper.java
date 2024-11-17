package com.siam.system.modular.mod_finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.system.modular.mod_finance.entity.FundFixedInvestmentPlan;
import com.siam.system.modular.mod_finance.model.example.FundFixedInvestmentPlanExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface FundFixedInvestmentPlanMapper extends BaseMapper<FundFixedInvestmentPlan> {
    int countByExample(FundFixedInvestmentPlanExample example);

    int deleteByExample(FundFixedInvestmentPlanExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FundFixedInvestmentPlan record);

    int insertSelective(FundFixedInvestmentPlan record);

    List<FundFixedInvestmentPlan> selectByExample(FundFixedInvestmentPlanExample example);

    FundFixedInvestmentPlan selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FundFixedInvestmentPlan record, @Param("example") FundFixedInvestmentPlanExample example);

    int updateByExample(@Param("record") FundFixedInvestmentPlan record, @Param("example") FundFixedInvestmentPlanExample example);

    int updateByPrimaryKeySelective(FundFixedInvestmentPlan record);

    int updateByPrimaryKey(FundFixedInvestmentPlan record);

    @ResultMap("BaseResultMap")
    @Select("<script>select ffip.* from tb_fund_fixed_investment_plan ffip" +
            "<where> 1=1 " +
            "<if test=\"fundFixedInvestmentPlan.id != null\"> AND ffip.id = #{fundFixedInvestmentPlan.id} </if>" +
            "<if test=\"fundFixedInvestmentPlan.memberId != null\"> AND ffip.member_id = #{fundFixedInvestmentPlan.memberId} </if>" +
            "<if test=\"fundFixedInvestmentPlan.fundIdComma != null and fundFixedInvestmentPlan.fundIdComma !=''\"> AND ffip.fund_id_comma like '%${fundFixedInvestmentPlan.fundIdComma}%' </if>" +
            "<if test=\"fundFixedInvestmentPlan.type != null\"> AND ffip.type = #{fundFixedInvestmentPlan.type} </if>" +
            "<if test=\"fundFixedInvestmentPlan.amount != null\"> AND ffip.amount = #{fundFixedInvestmentPlan.amount} </if>" +
            "<if test=\"fundFixedInvestmentPlan.cycle != null\"> AND ffip.cycle = #{fundFixedInvestmentPlan.cycle} </if>" +
            "<if test=\"fundFixedInvestmentPlan.expectedReturnRate != null\"> AND ffip.expected_return_rate = #{fundFixedInvestmentPlan.expectedReturnRate} </if>" +
            "<if test=\"fundFixedInvestmentPlan.status != null and fundFixedInvestmentPlan.status != -1\"> AND ffip.status = #{fundFixedInvestmentPlan.status} </if>" +
            "<if test=\"fundFixedInvestmentPlan.status == -1\"> AND (ffip.status = 0 or ffip.status = 1 or ffip.status = 2) </if>" +
            "<if test=\"fundFixedInvestmentPlan.actualReturnRate != null\"> AND ffip.actual_return_rate = #{fundFixedInvestmentPlan.actualReturnRate} </if>" +
            "<if test=\"fundFixedInvestmentPlan.income != null\"> AND ffip.income = #{fundFixedInvestmentPlan.income} </if>" +
            "<if test=\"fundFixedInvestmentPlan.remarks != null and fundFixedInvestmentPlan.remarks !=''\"> AND ffip.remarks like '%${fundFixedInvestmentPlan.remarks}%' </if>" +
            "</where> order by ffip.id asc" +
            "</script>")
    Page<FundFixedInvestmentPlan> getListByPage(@Param("page") Page page, @Param("fundFixedInvestmentPlan") FundFixedInvestmentPlan fundFixedInvestmentPlan);

    @ResultMap("CustomResultMap")
    @Select("<script>select ffip.* from tb_fund_fixed_investment_plan ffip" +
            "<where> 1=1 " +
            "<if test=\"fundFixedInvestmentPlan.id != null\"> AND ffip.id = #{fundFixedInvestmentPlan.id} </if>" +
            "<if test=\"fundFixedInvestmentPlan.memberId != null\"> AND ffip.member_id = #{fundFixedInvestmentPlan.memberId} </if>" +
            "<if test=\"fundFixedInvestmentPlan.fundIdComma != null and fundFixedInvestmentPlan.fundIdComma !=''\"> AND ffip.fund_id_comma like '%${fundFixedInvestmentPlan.fundIdComma}%' </if>" +
            "<if test=\"fundFixedInvestmentPlan.type != null\"> AND ffip.type = #{fundFixedInvestmentPlan.type} </if>" +
            "<if test=\"fundFixedInvestmentPlan.amount != null\"> AND ffip.amount = #{fundFixedInvestmentPlan.amount} </if>" +
            "<if test=\"fundFixedInvestmentPlan.cycle != null\"> AND ffip.cycle = #{fundFixedInvestmentPlan.cycle} </if>" +
            "<if test=\"fundFixedInvestmentPlan.expectedReturnRate != null\"> AND ffip.expected_return_rate = #{fundFixedInvestmentPlan.expectedReturnRate} </if>" +
            "<if test=\"fundFixedInvestmentPlan.status != null and fundFixedInvestmentPlan.status != -1\"> AND ffip.status = #{fundFixedInvestmentPlan.status} </if>" +
            "<if test=\"fundFixedInvestmentPlan.status == -1\"> AND (ffip.status = 0 or ffip.status = 1 or ffip.status = 2) </if>" +
            "<if test=\"fundFixedInvestmentPlan.actualReturnRate != null\"> AND ffip.actual_return_rate = #{fundFixedInvestmentPlan.actualReturnRate} </if>" +
            "<if test=\"fundFixedInvestmentPlan.income != null\"> AND ffip.income = #{fundFixedInvestmentPlan.income} </if>" +
            "<if test=\"fundFixedInvestmentPlan.remarks != null and fundFixedInvestmentPlan.remarks !=''\"> AND ffip.remarks like '%${fundFixedInvestmentPlan.remarks}%' </if>" +
            "</where> order by ffip.id asc" +
            "</script>")
    Page<Map<String, Object>> getMapListByPage(@Param("page") Page page, @Param("fundFixedInvestmentPlan") FundFixedInvestmentPlan fundFixedInvestmentPlan);
}