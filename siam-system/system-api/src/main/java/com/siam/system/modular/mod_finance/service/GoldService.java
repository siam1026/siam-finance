package com.siam.system.modular.mod_finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.system.modular.mod_finance.entity.Gold;
import com.siam.system.modular.mod_finance.model.example.GoldExample;

import java.util.List;

public interface GoldService {
    int countByExample(GoldExample example);

    void deleteByPrimaryKey(Integer id);

    void insertSelective(Gold record);

    List<Gold> selectByExample(GoldExample example);

    Gold selectByPrimaryKey(Integer id);

    void updateByExampleSelective(Gold record, GoldExample example);

    void updateByPrimaryKeySelective(Gold record);

    Page<Gold> getListByPage(int pageNo, int pageSize, Gold gold);

    /**
     * 更新股票价格
     *
     */
    void syncPrice();
}