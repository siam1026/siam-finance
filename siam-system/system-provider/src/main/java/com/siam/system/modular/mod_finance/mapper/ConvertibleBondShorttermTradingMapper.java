package com.siam.system.modular.mod_finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.system.modular.mod_finance.entity.ConvertibleBondShorttermTrading;
import com.siam.system.modular.mod_finance.model.param.ConvertibleBondShorttermTradingParam;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 可转债短期交易/做T交易记录表(一个做T买入记录可能对应多个做T卖出操作)数据层
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
public interface ConvertibleBondShorttermTradingMapper extends BaseMapper<ConvertibleBondShorttermTrading> {

    @ResultMap("BaseResultMap")
    @Select("<script>select fst.* from tb_convertible_bond_shortterm_trading fst" +
            "<where> 1=1 " +
            "<if test=\"convertibleBondShorttermTrading.id != null\"> AND fst.id = #{convertibleBondShorttermTrading.id} </if>" +
            "<if test=\"convertibleBondShorttermTrading.memberId != null\"> AND fst.member_id = #{convertibleBondShorttermTrading.memberId} </if>" +
            "<if test=\"convertibleBondShorttermTrading.convertibleBondId != null\"> AND fst.convertible_bond_id = #{convertibleBondShorttermTrading.convertibleBondId} </if>" +
            "<if test=\"convertibleBondShorttermTrading.type != null\"> AND fst.type = #{convertibleBondShorttermTrading.type} </if>" +
            "<if test=\"convertibleBondShorttermTrading.amount != null\"> AND fst.amount = #{convertibleBondShorttermTrading.amount} </if>" +
            "<if test=\"convertibleBondShorttermTrading.quantity != null\"> AND fst.quantity = #{convertibleBondShorttermTrading.quantity} </if>" +
            "<if test=\"convertibleBondShorttermTrading.price != null\"> AND fst.net_value = #{convertibleBondShorttermTrading.price} </if>" +
            "<if test=\"convertibleBondShorttermTrading.relatedTradingId != null\"> AND fst.related_trading_id = #{convertibleBondShorttermTrading.relatedTradingId} </if>" +
            "<if test=\"convertibleBondShorttermTrading.income != null\"> AND fst.income = #{convertibleBondShorttermTrading.income} </if>" +
            "<if test=\"convertibleBondShorttermTrading.status != null\"> AND fst.status = #{convertibleBondShorttermTrading.status} </if>" +
            "</where> order by fst.create_time desc" +
            "</script>")
    Page<ConvertibleBondShorttermTrading> getListByPage(@Param("page") Page page, @Param("convertibleBondShorttermTrading") ConvertibleBondShorttermTrading convertibleBondShorttermTrading);

    @ResultMap("CustomResultMap")
    @Select("<script>select fst.*, f.name as convertibleBondName from tb_convertible_bond_shortterm_trading fst left join tb_convertible_bond f on f.id = fst.convertible_bond_id" +
            "<where> 1=1 " +
            "<if test=\"convertibleBondShorttermTradingDto.id != null\"> AND fst.id = #{convertibleBondShorttermTradingDto.id} </if>" +
            "<if test=\"convertibleBondShorttermTradingDto.memberId != null\"> AND fst.member_id = #{convertibleBondShorttermTradingDto.memberId} </if>" +
            "<if test=\"convertibleBondShorttermTradingDto.convertibleBondId != null\"> AND fst.convertible_bond_id = #{convertibleBondShorttermTradingDto.convertibleBondId} </if>" +
            "<if test=\"convertibleBondShorttermTradingDto.type != null\"> AND fst.type = #{convertibleBondShorttermTradingDto.type} </if>" +
            "<if test=\"convertibleBondShorttermTradingDto.amount != null\"> AND fst.amount = #{convertibleBondShorttermTradingDto.amount} </if>" +
            "<if test=\"convertibleBondShorttermTradingDto.quantity != null\"> AND fst.quantity = #{convertibleBondShorttermTradingDto.quantity} </if>" +
            "<if test=\"convertibleBondShorttermTradingDto.price != null\"> AND fst.net_value = #{convertibleBondShorttermTradingDto.price} </if>" +
            "<if test=\"convertibleBondShorttermTradingDto.relatedTradingId != null\"> AND fst.related_trading_id = #{convertibleBondShorttermTradingDto.relatedTradingId} </if>" +
            "<if test=\"convertibleBondShorttermTradingDto.income != null\"> AND fst.income = #{convertibleBondShorttermTradingDto.income} </if>" +
            "<if test=\"convertibleBondShorttermTradingDto.status != null\"> AND fst.status = #{convertibleBondShorttermTradingDto.status} </if>" +
            "<if test=\"convertibleBondShorttermTradingDto.convertibleBondName != null and convertibleBondShorttermTradingDto.convertibleBondName !=''\"> AND f.name like '%${convertibleBondShorttermTradingDto.convertibleBondName}%' </if>" +
            "</where> order by fst.create_time desc" +
            "</script>")
    Page<Map<String, Object>> getListByPageJoinConvertibleBond(@Param("page") Page page, @Param("convertibleBondShorttermTradingDto") ConvertibleBondShorttermTradingParam param);

    @Select("select IFNULL(sum(fst.quantity), 0) from tb_convertible_bond_shortterm_trading fst where related_trading_id = #{relatedTradingId}")
    BigDecimal selectSumSellSharesByRelatedTradingId(@Param("relatedTradingId") Integer relatedTradingId);

    @Select("select IFNULL(sum(fst.amount), 0) from tb_convertible_bond_shortterm_trading fst where related_trading_id = #{relatedTradingId}")
    BigDecimal selectSumSellAmountByRelatedTradingId(@Param("relatedTradingId") Integer relatedTradingId);
}