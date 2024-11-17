package com.siam.system.modular.mod_finance.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.system.modular.mod_finance.entity.Gold;
import com.siam.system.modular.mod_finance.model.example.GoldExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface GoldMapper {
    int countByExample(GoldExample example);

    int deleteByExample(GoldExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Gold record);

    int insertSelective(Gold record);

    List<Gold> selectByExample(GoldExample example);

    Gold selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Gold record, @Param("example") GoldExample example);

    int updateByExample(@Param("record") Gold record, @Param("example") GoldExample example);

    int updateByPrimaryKeySelective(Gold record);

    int updateByPrimaryKey(Gold record);

    @ResultMap("BaseResultMap")
    @Select("<script>select g.* from tb_gold g" +
            "<where> 1=1 " +
            "<if test=\"gold.id != null\"> AND g.id = #{gold.id} </if>" +
            "<if test=\"gold.name != null and gold.name !=''\"> AND g.name like '%${gold.name}%' </if>" +
            "<if test=\"gold.lastestPrice != null\"> AND g.lastest_price = #{gold.lastestPrice} </if>" +
            "</where> order by g.id asc" +
            "</script>")
    Page<Gold> getListByPage(@Param("page") Page page, @Param("gold") Gold gold);
}