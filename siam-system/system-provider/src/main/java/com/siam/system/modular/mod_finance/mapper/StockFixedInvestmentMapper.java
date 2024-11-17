package com.siam.system.modular.mod_finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.system.modular.mod_finance.entity.StockFixedInvestment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * 股票定投交易记录表数据层
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
public interface StockFixedInvestmentMapper extends BaseMapper<StockFixedInvestment> {

    @ResultMap("BaseResultMap")
    @Select("<script>select ffi.* from tb_stock_fixed_investment ffi" +
            "<where> 1=1 " +
            "<if test=\"stockFixedInvestment.id != null\"> AND ffi.id = #{stockFixedInvestment.id} </if>" +
            "<if test=\"stockFixedInvestment.memberId != null\"> AND ffi.id = #{stockFixedInvestment.memberId} </if>" +
            "<if test=\"stockFixedInvestment.planId != null\"> AND ffi.plan_id = #{stockFixedInvestment.planId} </if>" +
            "<if test=\"stockFixedInvestment.stockId != null\"> AND ffi.stock_id = #{stockFixedInvestment.stockId} </if>" +
            "<if test=\"stockFixedInvestment.type != null\"> AND ffi.type = #{stockFixedInvestment.type} </if>" +
            "<if test=\"stockFixedInvestment.amount != null\"> AND ffi.amount = #{stockFixedInvestment.amount} </if>" +
            "<if test=\"stockFixedInvestment.serviceCharge != null\"> AND ffi.service_charge = #{stockFixedInvestment.serviceCharge} </if>" +
            "<if test=\"stockFixedInvestment.quantity != null\"> AND ffi.shares = #{stockFixedInvestment.quantity} </if>" +
            "<if test=\"stockFixedInvestment.price != null\"> AND ffi.net_value = #{stockFixedInvestment.price} </if>" +
            "<if test=\"stockFixedInvestment.income != null\"> AND ffi.income = #{stockFixedInvestment.income} </if>" +
            "<if test=\"stockFixedInvestment.returnRate != null\"> AND ffi.return_rate = #{stockFixedInvestment.returnRate} </if>" +
            "<if test=\"stockFixedInvestment.remarks != null and stockFixedInvestment.remarks !=''\"> AND ffi.remarks like '%${stockFixedInvestment.remarks}%' </if>" +
            "</where> order by ffi.id asc" +
            "</script>")
    Page<StockFixedInvestment> getListByPage(@Param("page") Page page, @Param("stockFixedInvestment") StockFixedInvestment stockFixedInvestment);

    @ResultMap("CustomResultMap")
    @Select("<script>select ffi.*, f.name as stockName from tb_stock_fixed_investment ffi left join tb_stock f on f.id = ffi.stock_id" +
            "<where> 1=1 " +
            "<if test=\"stockFixedInvestment.id != null\"> AND ffi.id = #{stockFixedInvestment.id} </if>" +
            "<if test=\"stockFixedInvestment.memberId != null\"> AND ffi.id = #{stockFixedInvestment.memberId} </if>" +
            "<if test=\"stockFixedInvestment.planId != null\"> AND ffi.plan_id = #{stockFixedInvestment.planId} </if>" +
            "<if test=\"stockFixedInvestment.stockId != null\"> AND ffi.stock_id = #{stockFixedInvestment.stockId} </if>" +
            "<if test=\"stockFixedInvestment.type != null\"> AND ffi.type = #{stockFixedInvestment.type} </if>" +
            "<if test=\"stockFixedInvestment.amount != null\"> AND ffi.amount = #{stockFixedInvestment.amount} </if>" +
            "<if test=\"stockFixedInvestment.serviceCharge != null\"> AND ffi.service_charge = #{stockFixedInvestment.serviceCharge} </if>" +
            "<if test=\"stockFixedInvestment.quantity != null\"> AND ffi.shares = #{stockFixedInvestment.quantity} </if>" +
            "<if test=\"stockFixedInvestment.price != null\"> AND ffi.net_value = #{stockFixedInvestment.price} </if>" +
            "<if test=\"stockFixedInvestment.income != null\"> AND ffi.income = #{stockFixedInvestment.income} </if>" +
            "<if test=\"stockFixedInvestment.returnRate != null\"> AND ffi.return_rate = #{stockFixedInvestment.returnRate} </if>" +
            "<if test=\"stockFixedInvestment.remarks != null and stockFixedInvestment.remarks !=''\"> AND ffi.remarks like '%${stockFixedInvestment.remarks}%' </if>" +
            "<if test=\"stockFixedInvestment.relatedTradingId != null\"> AND ffi.related_trading_id = #{stockFixedInvestment.relatedTradingId} </if>" +
            "<if test=\"stockFixedInvestment.status != null\"> AND ffi.status = #{stockFixedInvestment.status} </if>" +
            "</where> order by ffi.id asc" +
            "</script>")
    Page<Map<String, Object>> getMapListByPageJoinStock(@Param("page") Page page, @Param("stockFixedInvestment") StockFixedInvestment stockFixedInvestment);
}