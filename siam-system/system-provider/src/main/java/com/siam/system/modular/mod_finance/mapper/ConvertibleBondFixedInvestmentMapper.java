package com.siam.system.modular.mod_finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.system.modular.mod_finance.entity.ConvertibleBondFixedInvestment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * 可转债定投交易记录表数据层
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
public interface ConvertibleBondFixedInvestmentMapper extends BaseMapper<ConvertibleBondFixedInvestment> {

    @ResultMap("BaseResultMap")
    @Select("<script>select ffi.* from tb_convertible_bond_fixed_investment ffi" +
            "<where> 1=1 " +
            "<if test=\"convertibleBondFixedInvestment.id != null\"> AND ffi.id = #{convertibleBondFixedInvestment.id} </if>" +
            "<if test=\"convertibleBondFixedInvestment.memberId != null\"> AND ffi.member_id = #{convertibleBondFixedInvestment.memberId} </if>" +
            "<if test=\"convertibleBondFixedInvestment.planId != null\"> AND ffi.plan_id = #{convertibleBondFixedInvestment.planId} </if>" +
            "<if test=\"convertibleBondFixedInvestment.convertibleBondId != null\"> AND ffi.convertible_bond_id = #{convertibleBondFixedInvestment.convertibleBondId} </if>" +
            "<if test=\"convertibleBondFixedInvestment.type != null\"> AND ffi.type = #{convertibleBondFixedInvestment.type} </if>" +
            "<if test=\"convertibleBondFixedInvestment.amount != null\"> AND ffi.amount = #{convertibleBondFixedInvestment.amount} </if>" +
            "<if test=\"convertibleBondFixedInvestment.serviceCharge != null\"> AND ffi.service_charge = #{convertibleBondFixedInvestment.serviceCharge} </if>" +
            "<if test=\"convertibleBondFixedInvestment.quantity != null\"> AND ffi.shares = #{convertibleBondFixedInvestment.quantity} </if>" +
            "<if test=\"convertibleBondFixedInvestment.price != null\"> AND ffi.net_value = #{convertibleBondFixedInvestment.price} </if>" +
            "<if test=\"convertibleBondFixedInvestment.income != null\"> AND ffi.income = #{convertibleBondFixedInvestment.income} </if>" +
            "<if test=\"convertibleBondFixedInvestment.returnRate != null\"> AND ffi.return_rate = #{convertibleBondFixedInvestment.returnRate} </if>" +
            "<if test=\"convertibleBondFixedInvestment.remarks != null and convertibleBondFixedInvestment.remarks !=''\"> AND ffi.remarks like '%${convertibleBondFixedInvestment.remarks}%' </if>" +
            "</where> order by ffi.id asc" +
            "</script>")
    Page<ConvertibleBondFixedInvestment> getListByPage(@Param("page") Page page, @Param("convertibleBondFixedInvestment") ConvertibleBondFixedInvestment convertibleBondFixedInvestment);

    @ResultMap("CustomResultMap")
    @Select("<script>select ffi.*, f.name as convertibleBondName from tb_convertible_bond_fixed_investment ffi left join tb_convertible_bond f on f.id = ffi.convertible_bond_id" +
            "<where> 1=1 " +
            "<if test=\"convertibleBondFixedInvestment.id != null\"> AND ffi.id = #{convertibleBondFixedInvestment.id} </if>" +
            "<if test=\"convertibleBondFixedInvestment.memberId != null\"> AND ffi.member_id = #{convertibleBondFixedInvestment.memberId} </if>" +
            "<if test=\"convertibleBondFixedInvestment.planId != null\"> AND ffi.plan_id = #{convertibleBondFixedInvestment.planId} </if>" +
            "<if test=\"convertibleBondFixedInvestment.convertibleBondId != null\"> AND ffi.convertible_bond_id = #{convertibleBondFixedInvestment.convertibleBondId} </if>" +
            "<if test=\"convertibleBondFixedInvestment.type != null\"> AND ffi.type = #{convertibleBondFixedInvestment.type} </if>" +
            "<if test=\"convertibleBondFixedInvestment.amount != null\"> AND ffi.amount = #{convertibleBondFixedInvestment.amount} </if>" +
            "<if test=\"convertibleBondFixedInvestment.serviceCharge != null\"> AND ffi.service_charge = #{convertibleBondFixedInvestment.serviceCharge} </if>" +
            "<if test=\"convertibleBondFixedInvestment.quantity != null\"> AND ffi.shares = #{convertibleBondFixedInvestment.quantity} </if>" +
            "<if test=\"convertibleBondFixedInvestment.price != null\"> AND ffi.net_value = #{convertibleBondFixedInvestment.price} </if>" +
            "<if test=\"convertibleBondFixedInvestment.income != null\"> AND ffi.income = #{convertibleBondFixedInvestment.income} </if>" +
            "<if test=\"convertibleBondFixedInvestment.returnRate != null\"> AND ffi.return_rate = #{convertibleBondFixedInvestment.returnRate} </if>" +
            "<if test=\"convertibleBondFixedInvestment.remarks != null and convertibleBondFixedInvestment.remarks !=''\"> AND ffi.remarks like '%${convertibleBondFixedInvestment.remarks}%' </if>" +
            "<if test=\"convertibleBondFixedInvestment.relatedTradingId != null\"> AND ffi.related_trading_id = #{convertibleBondFixedInvestment.relatedTradingId} </if>" +
            "<if test=\"convertibleBondFixedInvestment.status != null\"> AND ffi.status = #{convertibleBondFixedInvestment.status} </if>" +
            "</where> order by ffi.id asc" +
            "</script>")
    Page<Map<String, Object>> getMapListByPageJoinConvertibleBond(@Param("page") Page page, @Param("convertibleBondFixedInvestment") ConvertibleBondFixedInvestment convertibleBondFixedInvestment);
}