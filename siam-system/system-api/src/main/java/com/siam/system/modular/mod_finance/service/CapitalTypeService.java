package com.siam.system.modular.mod_finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.siam.system.modular.mod_finance.entity.CapitalRecord;
import com.siam.system.modular.mod_finance.entity.CapitalType;
import com.siam.system.modular.mod_finance.model.example.CapitalTypeExample;

import java.util.List;

public interface CapitalTypeService extends IService<CapitalType> {

    void deleteByPrimaryKey(Integer id);

    void insertSelective(CapitalType record);

    CapitalType selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(CapitalType record);

    Page getListByPage(int pageNo, int pageSize, CapitalType capitalType);

    CapitalType selectByName(String name);
}