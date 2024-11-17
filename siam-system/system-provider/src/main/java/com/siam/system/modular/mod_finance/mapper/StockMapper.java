package com.siam.system.modular.mod_finance.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.system.config.BusinessBaseMapper;
import com.siam.system.config.BusinessBaseMapper;
import com.siam.system.modular.mod_finance.entity.Stock;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 股票信息表数据层
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
public interface StockMapper extends BusinessBaseMapper<Stock> {

    @ResultMap("BaseResultMap")
    @Select("<script>select f.* from tb_stock f" +
            "<where> 1=1 " +
            "<if test=\"stock.id != null\"> AND f.id = #{stock.id} </if>" +
            "<if test=\"stock.name != null and stock.name !=''\"> AND f.name like '%${stock.name}%' </if>" +
            "<if test=\"stock.code != null and stock.code !=''\"> AND f.code like '%${stock.code}%' </if>" +
            "<if test=\"stock.lastestPrice != null\"> AND f.lastest_price = #{stock.lastestPrice} </if>" +
            "<if test=\"stock.isOptional != null\"> AND f.is_optional = #{stock.isOptional} </if>" +
            "</where> order by f.id asc" +
            "</script>")
    Page<Map<String, Object>> getListByPage(@Param("page") Page page, @Param("stock") Stock stock);

    /**
     * 批量更新
     *
     */
    Integer updateBatchSomeColumn(@Param("idList") List<Integer> idList, @Param("entityMap") Map entityMap);
}