package com.siam.system.modular.mod_finance.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.system.config.BusinessBaseMapper;
import com.siam.system.modular.mod_finance.entity.ConvertibleBond;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * 可转债信息表数据层
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
public interface ConvertibleBondMapper extends BusinessBaseMapper<ConvertibleBond> {

    @ResultMap("BaseResultMap")
    @Select("<script>select f.* from tb_convertible_bond f" +
            "<where> 1=1 " +
            "<if test=\"convertibleBond.id != null\"> AND f.id = #{convertibleBond.id} </if>" +
            "<if test=\"convertibleBond.name != null and convertibleBond.name !=''\"> AND f.name like '%${convertibleBond.name}%' </if>" +
            "<if test=\"convertibleBond.code != null and convertibleBond.code !=''\"> AND f.code like '%${convertibleBond.code}%' </if>" +
            "<if test=\"convertibleBond.lastestPrice != null\"> AND f.lastest_price = #{convertibleBond.lastestPrice} </if>" +
            "<if test=\"convertibleBond.isOptional != null\"> AND f.is_optional = #{convertibleBond.isOptional} </if>" +
            "</where> order by f.id asc" +
            "</script>")
    Page<Map<String, Object>> getListByPage(@Param("page") Page page, @Param("convertibleBond") ConvertibleBond convertibleBond);
}