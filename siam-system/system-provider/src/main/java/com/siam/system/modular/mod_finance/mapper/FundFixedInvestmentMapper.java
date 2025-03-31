package com.siam.system.modular.mod_finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.system.modular.mod_finance.entity.FundFixedInvestment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

public interface FundFixedInvestmentMapper extends BaseMapper<FundFixedInvestment> {
//    int countByExample(FundFixedInvestmentExample example);
//
//    int deleteByExample(FundFixedInvestmentExample example);
//
//    int deleteByPrimaryKey(Integer id);
//
//    int insert(FundFixedInvestment record);
//
//    int insertSelective(FundFixedInvestment record);
//
//    List<FundFixedInvestment> selectByExample(FundFixedInvestmentExample example);
//
//    FundFixedInvestment selectByPrimaryKey(Integer id);

//    int updateByExampleSelective(@Param("record") FundFixedInvestment record, @Param("example") FundFixedInvestmentExample example);

//    int updateByExample(@Param("record") FundFixedInvestment record, @Param("example") FundFixedInvestmentExample example);

//    int updateByPrimaryKeySelective(FundFixedInvestment record);

//    int updateByPrimaryKey(FundFixedInvestment record);

    @ResultMap("BaseResultMap")
    @Select("<script>select ffi.* from tb_fund_fixed_investment ffi" +
            "<where> 1=1 " +
            "<if test=\"fundFixedInvestment.id != null\"> AND ffi.id = #{fundFixedInvestment.id} </if>" +
            "<if test=\"fundFixedInvestment.memberId != null\"> AND ffi.member_id = #{fundFixedInvestment.memberId} </if>" +
            "<if test=\"fundFixedInvestment.planId != null\"> AND ffi.plan_id = #{fundFixedInvestment.planId} </if>" +
            "<if test=\"fundFixedInvestment.fundId != null\"> AND ffi.fund_id = #{fundFixedInvestment.fundId} </if>" +
            "<if test=\"fundFixedInvestment.type != null\"> AND ffi.type = #{fundFixedInvestment.type} </if>" +
            "<if test=\"fundFixedInvestment.amount != null\"> AND ffi.amount = #{fundFixedInvestment.amount} </if>" +
            "<if test=\"fundFixedInvestment.serviceCharge != null\"> AND ffi.service_charge = #{fundFixedInvestment.serviceCharge} </if>" +
            "<if test=\"fundFixedInvestment.shares != null\"> AND ffi.shares = #{fundFixedInvestment.shares} </if>" +
            "<if test=\"fundFixedInvestment.netValue != null\"> AND ffi.net_value = #{fundFixedInvestment.netValue} </if>" +
            "<if test=\"fundFixedInvestment.income != null\"> AND ffi.income = #{fundFixedInvestment.income} </if>" +
            "<if test=\"fundFixedInvestment.returnRate != null\"> AND ffi.return_rate = #{fundFixedInvestment.returnRate} </if>" +
            "<if test=\"fundFixedInvestment.remarks != null and fundFixedInvestment.remarks !=''\"> AND ffi.remarks like '%${fundFixedInvestment.remarks}%' </if>" +
            "</where> order by ffi.id asc" +
            "</script>")
    Page<FundFixedInvestment> getListByPage(@Param("page") Page page, @Param("fundFixedInvestment") FundFixedInvestment fundFixedInvestment);

    @ResultMap("CustomResultMap")
    @Select("<script>select ffi.*, f.name as fundName from tb_fund_fixed_investment ffi left join tb_fund f on f.id = ffi.fund_id" +
            "<where> 1=1 " +
            "<if test=\"fundFixedInvestment.id != null\"> AND ffi.id = #{fundFixedInvestment.id} </if>" +
            "<if test=\"fundFixedInvestment.memberId != null\"> AND ffi.member_id = #{fundFixedInvestment.memberId} </if>" +
            "<if test=\"fundFixedInvestment.planId != null\"> AND ffi.plan_id = #{fundFixedInvestment.planId} </if>" +
            "<if test=\"fundFixedInvestment.fundId != null\"> AND ffi.fund_id = #{fundFixedInvestment.fundId} </if>" +
            "<if test=\"fundFixedInvestment.type != null\"> AND ffi.type = #{fundFixedInvestment.type} </if>" +
            "<if test=\"fundFixedInvestment.amount != null\"> AND ffi.amount = #{fundFixedInvestment.amount} </if>" +
            "<if test=\"fundFixedInvestment.serviceCharge != null\"> AND ffi.service_charge = #{fundFixedInvestment.serviceCharge} </if>" +
            "<if test=\"fundFixedInvestment.shares != null\"> AND ffi.shares = #{fundFixedInvestment.shares} </if>" +
            "<if test=\"fundFixedInvestment.netValue != null\"> AND ffi.net_value = #{fundFixedInvestment.netValue} </if>" +
            "<if test=\"fundFixedInvestment.income != null\"> AND ffi.income = #{fundFixedInvestment.income} </if>" +
            "<if test=\"fundFixedInvestment.returnRate != null\"> AND ffi.return_rate = #{fundFixedInvestment.returnRate} </if>" +
            "<if test=\"fundFixedInvestment.remarks != null and fundFixedInvestment.remarks !=''\"> AND ffi.remarks like '%${fundFixedInvestment.remarks}%' </if>" +
            "<if test=\"fundFixedInvestment.relatedTradingId != null\"> AND ffi.related_trading_id = #{fundFixedInvestment.relatedTradingId} </if>" +
            "<if test=\"fundFixedInvestment.status != null\"> AND ffi.status = #{fundFixedInvestment.status} </if>" +
            "</where> order by ffi.id asc" +
            "</script>")
    Page<Map<String, Object>> getMapListByPageJoinFund(@Param("page") Page page, @Param("fundFixedInvestment") FundFixedInvestment fundFixedInvestment);
}