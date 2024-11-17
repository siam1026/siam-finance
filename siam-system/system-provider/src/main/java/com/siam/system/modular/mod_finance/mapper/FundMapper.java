package com.siam.system.modular.mod_finance.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.system.config.BusinessBaseMapper;
import com.siam.system.modular.mod_finance.entity.Fund;
import com.siam.system.modular.mod_finance.model.example.FundExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface FundMapper extends BusinessBaseMapper<Fund> {
    int countByExample(FundExample example);

    int deleteByExample(FundExample example);

    int deleteByPrimaryKey(Integer id);

    /*int insert(Fund record);*/

    /*int insertSelective(Fund record);*/

    List<Fund> selectByExample(FundExample example);

    Fund selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Fund record, @Param("example") FundExample example);

    int updateByExample(@Param("record") Fund record, @Param("example") FundExample example);

    int updateByPrimaryKeySelective(Fund record);

    int updateByPrimaryKey(Fund record);

    @ResultMap("BaseResultMap")
    @Select("<script>select f.* from tb_fund f" +
            "<where> 1=1 " +
            "<if test=\"fund.id != null\"> AND f.id = #{fund.id} </if>" +
            "<if test=\"fund.name != null and fund.name !=''\"> AND f.name like '%${fund.name}%' </if>" +
            "<if test=\"fund.code != null and fund.code !=''\"> AND f.code like '%${fund.code}%' </if>" +
            "<if test=\"fund.lastestNetValue != null\"> AND f.lastest_net_value = #{fund.lastestNetValue} </if>" +
            "<if test=\"fund.isOptional != null\"> AND f.is_optional = #{fund.isOptional} </if>" +
            "</where> order by f.id asc" +
            "</script>")
    Page<Map<String, Object>> getListByPage(@Param("page") Page page, @Param("fund") Fund fund);
}