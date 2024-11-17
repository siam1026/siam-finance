package com.siam.system.modular.mod_finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.system.modular.mod_finance.entity.CapitalType;
import com.siam.system.modular.mod_finance.model.example.CapitalTypeExample;

import java.util.List;

public interface CapitalTypeService {
    int countByExample(CapitalTypeExample example);

    void deleteByPrimaryKey(Integer id);

    void insertSelective(CapitalType record);

    List<CapitalType> selectByExample(CapitalTypeExample example);

    CapitalType selectByPrimaryKey(Integer id);

    void updateByExampleSelective(CapitalType record, CapitalTypeExample example);

    void updateByPrimaryKeySelective(CapitalType record);

    Page getListByPage(int pageNo, int pageSize, CapitalType capitalType);

    CapitalType selectByName(String name);
}