package com.siam.system.modular.mod_finance.model.param;

import com.siam.system.modular.mod_finance.entity.Fund;
import lombok.Data;

@Data
public class CommonParam extends Fund {

    //是否加密返回数据 0=否 1=是
    private Boolean isEncrypt;

    //页码
    private Integer pageNo = 1;

    //页面大小
    private Integer pageSize = 20;

}