package com.siam.system.modular.mod_finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.siam.system.modular.mod_finance.entity.ConvertibleBond;
import com.siam.system.modular.mod_finance.model.param.ConvertibleBondParam;

import java.util.Collection;
import java.util.Map;

/**
 * 可转债信息表业务层
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
public interface ConvertibleBondService extends IService<ConvertibleBond> {

    Page getListByPage(int pageNo, int pageSize, ConvertibleBond fund);

    /**
     * 同步可转债列表
     *
     */
    void syncConvertibleBondList();

    /**
     * 更新可转债价格
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
    void insert(ConvertibleBondParam param);

    /**
     * 修改接口
     *
     * @author JiangP
     */
    void update(ConvertibleBondParam param);

    /**
     * 删除接口
     *
     * @author JiangP
     */
    void delete(ConvertibleBondParam param);

    /**
     * 基金-统计分析接口
     *
     * @author JiangP
     */
    Map<String, Object> statistics(ConvertibleBondParam param);

    /**
     * 批量插入
     *
     */
    Integer insertBatchSomeColumn(Collection<ConvertibleBond> entityList);
}