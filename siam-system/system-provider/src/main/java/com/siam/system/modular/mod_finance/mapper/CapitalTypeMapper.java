package com.siam.system.modular.mod_finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.system.modular.mod_finance.entity.CapitalRecord;
import com.siam.system.modular.mod_finance.entity.CapitalType;
import com.siam.system.modular.mod_finance.model.example.CapitalTypeExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface CapitalTypeMapper extends BaseMapper<CapitalType> {

    @ResultMap("BaseResultMap")
    @Select("<script>select ct.* from tb_capital_type ct" +
            "<where> 1=1 " +
            "<if test=\"capitalType.id != null\"> AND ct.id = #{capitalType.id} </if>" +
            "<if test=\"capitalType.memberId != null\"> AND ct.member_id = #{capitalType.memberId} </if>" +
            "<if test=\"capitalType.name != null and capitalType.name !=''\"> AND ct.name like '%${capitalType.name}%' </if>" +
            "<if test=\"capitalType.description != null and capitalType.description !=''\"> AND ct.description like '%${capitalType.description}%' </if>" +
            "</where> order by ct.id asc" +
            "</script>")
    Page<Map<String, Object>> getListByPage(@Param("page") Page page, @Param("capitalType") CapitalType capitalType);

    @ResultMap("BaseResultMap")
    @Select("select ct.* from tb_capital_type ct where ct.name = #{name} and ct.member_id = #{memberId} limit 1")
    CapitalType selectByName(@Param("name") String name, @Param("memberId") Integer memberId);
}