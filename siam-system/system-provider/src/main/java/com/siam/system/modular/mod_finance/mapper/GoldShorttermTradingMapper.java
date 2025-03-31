package com.siam.system.modular.mod_finance.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.system.modular.mod_finance.entity.GoldShorttermTrading;
import com.siam.system.modular.mod_finance.model.example.GoldShorttermTradingExample;
import com.siam.system.modular.mod_finance.model.result.GoldShorttermTradingResult;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

public interface GoldShorttermTradingMapper {
    int countByExample(GoldShorttermTradingExample example);

    int deleteByExample(GoldShorttermTradingExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(GoldShorttermTrading record);

    int insertSelective(GoldShorttermTrading record);

    List<GoldShorttermTrading> selectByExample(GoldShorttermTradingExample example);

    GoldShorttermTrading selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") GoldShorttermTrading record, @Param("example") GoldShorttermTradingExample example);

    int updateByExample(@Param("record") GoldShorttermTrading record, @Param("example") GoldShorttermTradingExample example);

    int updateByPrimaryKeySelective(GoldShorttermTrading record);

    int updateByPrimaryKey(GoldShorttermTrading record);

    @ResultMap("BaseResultMap")
    @Select("<script>select gst.* from tb_gold_shortterm_trading gst" +
            "<where> 1=1 " +
            "<if test=\"goldShorttermTrading.id != null\"> AND gst.id = #{goldShorttermTrading.id} </if>" +
            "<if test=\"goldShorttermTrading.memberId != null\"> AND gst.member_id = #{goldShorttermTrading.memberId} </if>" +
            "<if test=\"goldShorttermTrading.goldId != null\"> AND gst.gold_id = #{goldShorttermTrading.goldId} </if>" +
            "<if test=\"goldShorttermTrading.type != null\"> AND gst.type = #{goldShorttermTrading.type} </if>" +
            "<if test=\"goldShorttermTrading.amount != null\"> AND gst.amount = #{goldShorttermTrading.amount} </if>" +
            "<if test=\"goldShorttermTrading.grams != null\"> AND gst.grams = #{goldShorttermTrading.grams} </if>" +
            "<if test=\"goldShorttermTrading.price != null\"> AND gst.price = #{goldShorttermTrading.price} </if>" +
            "<if test=\"goldShorttermTrading.relatedTradingId != null\"> AND gst.related_trading_id = #{goldShorttermTrading.relatedTradingId} </if>" +
            "<if test=\"goldShorttermTrading.income != null\"> AND gst.income = #{goldShorttermTrading.income} </if>" +
            "<if test=\"goldShorttermTrading.status != null\"> AND gst.status = #{goldShorttermTrading.status} </if>" +
            "</where> order by gst.id asc" +
            "</script>")
    Page<GoldShorttermTradingResult> getListByPage(@Param("page") Page page, @Param("goldShorttermTrading") GoldShorttermTrading goldShorttermTrading);

    @Select("select IFNULL(sum(gst.grams), 0) from tb_gold_shortterm_trading gst where related_trading_id = #{relatedTradingId}")
    BigDecimal selectSumGramsByRelatedTradingId(@Param("relatedTradingId") Integer relatedTradingId);

    @Select("select IFNULL(sum(gst.amount), 0) from tb_gold_shortterm_trading gst where related_trading_id = #{relatedTradingId}")
    BigDecimal selectSumAmountByRelatedTradingId(@Param("relatedTradingId") Integer relatedTradingId);

    @Select("select IFNULL(sum(fst.grams), 0) from tb_gold_shortterm_trading gst where related_trading_id = #{relatedTradingId}")
    BigDecimal selectSumSellSharesByRelatedTradingId(@Param("relatedTradingId") Integer relatedTradingId);
}