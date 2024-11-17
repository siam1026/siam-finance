package com.siam.system.modular.mod_finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.system.modular.mod_finance.entity.FundShorttermTrading;
import com.siam.system.modular.mod_finance.model.example.FundShorttermTradingExample;
import com.siam.system.modular.mod_finance.model.param.FundShorttermTradingParam;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface FundShorttermTradingMapper extends BaseMapper<FundShorttermTrading> {
    int countByExample(FundShorttermTradingExample example);

    int deleteByExample(FundShorttermTradingExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FundShorttermTrading record);

    int insertSelective(FundShorttermTrading record);

    List<FundShorttermTrading> selectByExample(FundShorttermTradingExample example);

    FundShorttermTrading selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FundShorttermTrading record, @Param("example") FundShorttermTradingExample example);

    int updateByExample(@Param("record") FundShorttermTrading record, @Param("example") FundShorttermTradingExample example);

    int updateByPrimaryKeySelective(FundShorttermTrading record);

    int updateByPrimaryKey(FundShorttermTrading record);

    @ResultMap("BaseResultMap")
    @Select("<script>select fst.* from tb_fund_shortterm_trading fst" +
            "<where> 1=1 " +
            "<if test=\"fundShorttermTrading.id != null\"> AND fst.id = #{fundShorttermTrading.id} </if>" +
            "<if test=\"fundShorttermTrading.memberId != null\"> AND fst.member_id = #{fundShorttermTrading.memberId} </if>" +
            "<if test=\"fundShorttermTrading.fundId != null\"> AND fst.fund_id = #{fundShorttermTrading.fundId} </if>" +
            "<if test=\"fundShorttermTrading.type != null\"> AND fst.type = #{fundShorttermTrading.type} </if>" +
            "<if test=\"fundShorttermTrading.amount != null\"> AND fst.amount = #{fundShorttermTrading.amount} </if>" +
            "<if test=\"fundShorttermTrading.shares != null\"> AND fst.shares = #{fundShorttermTrading.shares} </if>" +
            "<if test=\"fundShorttermTrading.netValue != null\"> AND fst.net_value = #{fundShorttermTrading.netValue} </if>" +
            "<if test=\"fundShorttermTrading.relatedTradingId != null\"> AND fst.related_trading_id = #{fundShorttermTrading.relatedTradingId} </if>" +
            "<if test=\"fundShorttermTrading.income != null\"> AND fst.income = #{fundShorttermTrading.income} </if>" +
            "<if test=\"fundShorttermTrading.status != null\"> AND fst.status = #{fundShorttermTrading.status} </if>" +
            "</where> order by fst.create_time desc" +
            "</script>")
    Page<FundShorttermTrading> getListByPage(@Param("page") Page page, @Param("fundShorttermTrading") FundShorttermTrading fundShorttermTrading);

    @ResultMap("CustomResultMap")
    @Select("<script>select fst.*, f.name as fundName from tb_fund_shortterm_trading fst left join tb_fund f on f.id = fst.fund_id" +
            "<where> 1=1 " +
            "<if test=\"fundShorttermTradingDto.id != null\"> AND fst.id = #{fundShorttermTradingDto.id} </if>" +
            "<if test=\"fundShorttermTradingDto.memberId != null\"> AND fst.member_id = #{fundShorttermTradingDto.memberId} </if>" +
            "<if test=\"fundShorttermTradingDto.fundId != null\"> AND fst.fund_id = #{fundShorttermTradingDto.fundId} </if>" +
            "<if test=\"fundShorttermTradingDto.type != null\"> AND fst.type = #{fundShorttermTradingDto.type} </if>" +
            "<if test=\"fundShorttermTradingDto.amount != null\"> AND fst.amount = #{fundShorttermTradingDto.amount} </if>" +
            "<if test=\"fundShorttermTradingDto.shares != null\"> AND fst.shares = #{fundShorttermTradingDto.shares} </if>" +
            "<if test=\"fundShorttermTradingDto.netValue != null\"> AND fst.net_value = #{fundShorttermTradingDto.netValue} </if>" +
            "<if test=\"fundShorttermTradingDto.relatedTradingId != null\"> AND fst.related_trading_id = #{fundShorttermTradingDto.relatedTradingId} </if>" +
            "<if test=\"fundShorttermTradingDto.income != null\"> AND fst.income = #{fundShorttermTradingDto.income} </if>" +
            "<if test=\"fundShorttermTradingDto.status != null\"> AND fst.status = #{fundShorttermTradingDto.status} </if>" +
            "<if test=\"fundShorttermTradingDto.fundName != null and fundShorttermTradingDto.fundName !=''\"> AND f.name like '%${fundShorttermTradingDto.fundName}%' </if>" +
            "</where> order by fst.create_time desc" +
            "</script>")
    Page<Map<String, Object>> getListByPageJoinFund(@Param("page") Page page, @Param("fundShorttermTradingDto") FundShorttermTradingParam param);

    @Select("select IFNULL(sum(fst.shares), 0) from tb_fund_shortterm_trading fst where related_trading_id = #{relatedTradingId}")
    BigDecimal selectSumSellSharesByRelatedTradingId(@Param("relatedTradingId") Integer relatedTradingId);

    @Select("select IFNULL(sum(fst.amount), 0) from tb_fund_shortterm_trading fst where related_trading_id = #{relatedTradingId}")
    BigDecimal selectSumSellAmountByRelatedTradingId(@Param("relatedTradingId") Integer relatedTradingId);
}