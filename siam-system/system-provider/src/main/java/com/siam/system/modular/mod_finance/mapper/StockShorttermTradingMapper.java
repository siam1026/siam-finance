package com.siam.system.modular.mod_finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.system.modular.mod_finance.entity.StockShorttermTrading;
import com.siam.system.modular.mod_finance.model.param.StockShorttermTradingParam;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 股票短期交易/做T交易记录表(一个做T买入记录可能对应多个做T卖出操作)数据层
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
public interface StockShorttermTradingMapper extends BaseMapper<StockShorttermTrading> {

    @ResultMap("BaseResultMap")
    @Select("<script>select fst.* from tb_stock_shortterm_trading fst" +
            "<where> 1=1 " +
            "<if test=\"stockShorttermTrading.id != null\"> AND fst.id = #{stockShorttermTrading.id} </if>" +
            "<if test=\"stockShorttermTrading.memberId != null\"> AND fst.member_id = #{stockShorttermTrading.memberId} </if>" +
            "<if test=\"stockShorttermTrading.stockId != null\"> AND fst.stock_id = #{stockShorttermTrading.stockId} </if>" +
            "<if test=\"stockShorttermTrading.type != null\"> AND fst.type = #{stockShorttermTrading.type} </if>" +
            "<if test=\"stockShorttermTrading.amount != null\"> AND fst.amount = #{stockShorttermTrading.amount} </if>" +
            "<if test=\"stockShorttermTrading.quantity != null\"> AND fst.quantity = #{stockShorttermTrading.quantity} </if>" +
            "<if test=\"stockShorttermTrading.price != null\"> AND fst.net_value = #{stockShorttermTrading.price} </if>" +
            "<if test=\"stockShorttermTrading.relatedTradingId != null\"> AND fst.related_trading_id = #{stockShorttermTrading.relatedTradingId} </if>" +
            "<if test=\"stockShorttermTrading.income != null\"> AND fst.income = #{stockShorttermTrading.income} </if>" +
            "<if test=\"stockShorttermTrading.status != null\"> AND fst.status = #{stockShorttermTrading.status} </if>" +
            "</where> order by fst.create_time desc" +
            "</script>")
    Page<StockShorttermTrading> getListByPage(@Param("page") Page page, @Param("stockShorttermTrading") StockShorttermTrading stockShorttermTrading);

    @ResultMap("CustomResultMap")
    @Select("<script>select fst.*, f.name as stockName from tb_stock_shortterm_trading fst left join tb_stock f on f.id = fst.stock_id" +
            "<where> 1=1 " +
            "<if test=\"stockShorttermTradingDto.id != null\"> AND fst.id = #{stockShorttermTradingDto.id} </if>" +
            "<if test=\"stockShorttermTrading.memberId != null\"> AND fst.member_id = #{stockShorttermTrading.memberId} </if>" +
            "<if test=\"stockShorttermTrading.stockId != null\"> AND fst.stock_id = #{stockShorttermTrading.stockId} </if>" +
            "<if test=\"stockShorttermTradingDto.type != null\"> AND fst.type = #{stockShorttermTradingDto.type} </if>" +
            "<if test=\"stockShorttermTradingDto.amount != null\"> AND fst.amount = #{stockShorttermTradingDto.amount} </if>" +
            "<if test=\"stockShorttermTradingDto.quantity != null\"> AND fst.quantity = #{stockShorttermTradingDto.quantity} </if>" +
            "<if test=\"stockShorttermTradingDto.price != null\"> AND fst.net_value = #{stockShorttermTradingDto.price} </if>" +
            "<if test=\"stockShorttermTradingDto.relatedTradingId != null\"> AND fst.related_trading_id = #{stockShorttermTradingDto.relatedTradingId} </if>" +
            "<if test=\"stockShorttermTradingDto.income != null\"> AND fst.income = #{stockShorttermTradingDto.income} </if>" +
            "<if test=\"stockShorttermTradingDto.status != null\"> AND fst.status = #{stockShorttermTradingDto.status} </if>" +
            "<if test=\"stockShorttermTradingDto.stockName != null and stockShorttermTradingDto.stockName !=''\"> AND f.name like '%${stockShorttermTradingDto.stockName}%' </if>" +
            "</where> order by fst.create_time desc" +
            "</script>")
    Page<Map<String, Object>> getListByPageJoinStock(@Param("page") Page page, @Param("stockShorttermTradingDto") StockShorttermTradingParam param);

    @Select("select IFNULL(sum(fst.quantity), 0) from tb_stock_shortterm_trading fst where related_trading_id = #{relatedTradingId}")
    BigDecimal selectSumSellSharesByRelatedTradingId(@Param("relatedTradingId") Integer relatedTradingId);

    @Select("select IFNULL(sum(fst.amount), 0) from tb_stock_shortterm_trading fst where related_trading_id = #{relatedTradingId}")
    BigDecimal selectSumSellAmountByRelatedTradingId(@Param("relatedTradingId") Integer relatedTradingId);
}