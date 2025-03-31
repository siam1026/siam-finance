package com.siam.system.modular.mod_finance.model.result;

import com.siam.system.modular.mod_finance.entity.ConvertibleBond;
import lombok.Data;

import java.util.Date;

/**
 * 可转债信息表实例类
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
@Data
public class ConvertibleBondResult extends ConvertibleBond {

    //开始日期
    private Date startCreateTime;

    //结束日期
    private Date endCreateTime;

    //页码
    private Integer pageNo = 1;

    //页面大小
    private Integer pageSize = 20;
}