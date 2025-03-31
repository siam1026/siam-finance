package com.siam.system.modular.mod_finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.siam.system.modular.mod_finance.entity.Stock;
import com.siam.system.modular.mod_finance.model.param.StockParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 股票信息表业务层
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
public interface StockService extends IService<Stock> {

    Page getListByPage(int pageNo, int pageSize, Stock stock);

    /**
     * 同步股票列表
     *
     */
    void syncStockList();

    /**
     * 更新股票价格
     *
     */
    void syncPrice(Boolean isOptional);

    /**
     * 监控股价(依靠程序进行盯盘)，股票价格涨跌幅达到设定比例则推送通知
     *
     */
    void monitorPrice(Boolean isOptional);

    /**
     * 新增接口
     *
     * @author JiangP
     */
    void insert(StockParam param);

    /**
     * 修改接口
     *
     * @author JiangP
     */
    void update(StockParam param);

    /**
     * 删除接口
     *
     * @author JiangP
     */
    void delete(StockParam param);

    /**
     * 基金-统计分析接口
     *
     * @author JiangP
     */
    Map<String, Object> statistics(StockParam param);

    /**
     * 批量插入
     *
     */
    Integer insertBatchSomeColumn(Collection<Stock> entityList);

    /**
     * 批量更新 - TODO(待测试)
     *
     */
    Integer updateBatchSomeColumn(List<Stock> entityList);
}