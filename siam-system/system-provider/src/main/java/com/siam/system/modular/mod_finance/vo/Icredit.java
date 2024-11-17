package com.siam.system.modular.mod_finance.vo;

import lombok.Data;

import java.util.List;

/**
 * 新浪基金实体对象类
 *
 */
@Data
public class Icredit {

    private String 基金数据实体信息状态;

    private String 基金数据实体数量;

    private List<FundInfo> 基金数据实体信息;

    @Data
    public static class FundInfo {

        private String 基金代码;

        private String 基金简称;

        private String 基金简称_精简;
    }
}