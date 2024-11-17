package com.siam.system.modular.mod_finance.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 新浪基金实体对象类
 *
 */
@Data
public class YiYuanData {

    //总数量
    private Integer allNum;

    //总页数
    private Integer allPages;

    //当前页面
    private Integer currentPage;

    //内容列表
    private List<StockInfo> contentlist;

    private Integer maxResult;

    private Integer ret_code;

    @Data
    public static class StockInfo {

        /**
         * 股票名称
         */
        private String name;

        /**
         * 股票代码
         */
        private String code;

        /**
         * 市场简写。支持 sh、sz、hk
         */
        private String market;

        /**
         * 1为上市，其他下市
         */
        private Integer state;

        /**
         * 流通股本，万股
         */
        private BigDecimal currcapital;

        /**
         * 四季度净利润（亿元）
         */
        private BigDecimal profit_four;

        /**
         * 上市日期
         */
        private String listing_date;

        /**
         * 总股本，万股
         */
        private BigDecimal totalcapital;

        /**
         * 每股净资产（元）
         */
        private BigDecimal mgjzc;

        /**
         * 拼音
         */
        private String pinyin;

        private String stockType;

        private String remark;

        private String remarks;

        private String ret_code;

        private String is_quit_market;

        private Object ct;

        private Object _id;
    }
}