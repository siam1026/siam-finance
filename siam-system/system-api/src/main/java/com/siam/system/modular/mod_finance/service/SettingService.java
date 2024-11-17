package com.siam.system.modular.mod_finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.siam.system.modular.mod_finance.entity.Setting;
import com.siam.system.modular.mod_finance.model.param.SettingParam;

/**
 * 基础数据设置表业务层
 *
 * @author JiangP
 * @date 2022/01/23 22:04
 */
public interface SettingService extends IService<Setting> {

    /**
     * 查询基础数据设置
     * @author JiangP
     */
    Setting selectCurrent();

    /**
     * 修改接口
     *
     * @author JiangP
     */
    void update(SettingParam param);
}