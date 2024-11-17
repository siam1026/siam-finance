package com.siam.system.modular.mod_finance.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 新浪基金实体对象类
 *
 */
@Data
public class Jisilu {

    private Integer page;

    private List<ConvertibleBondInfo> rows;

    private Integer total;

    @Data
    public static class ConvertibleBondInfo {

        private String id;

        private Cell cell;

    }

    @Data
    public static class Cell {

        //代码
        private String bond_id;

        //转债名称
        private String bond_nm;

        //拼音
        private String bond_py;

        //现价
        private BigDecimal price;

        //涨跌幅(%)
        private BigDecimal increase_rt;

        //正股代码
        private String stock_id;

        //正股名称
        private String stock_nm;

        //正股拼音
        private String stock_py;

        //正股价
        private BigDecimal sprice;

        //正股涨跌
        private BigDecimal sincrease_rt;

        //正股PB
        private BigDecimal pb;

        //转股价
        private BigDecimal convert_price;

        //转股价值
        private BigDecimal convert_value;

        //
        private String convert_dt;

        //溢价率
        private BigDecimal premium_rt;

        //
        private String dblow;

        //
        private String adjust_condition;

        //
        private String sw_cd;

        //
        private String market_cd;

        //
        private String btype;

        //
        private String list_dt;

        //
        private String qflag2;

        //
        private String owned;

        //
        private String hold;

        //
        private String bond_value;

        //债券评级
        private String rating_cd;

        //
        private String option_value;

        //回售触发价
        private BigDecimal put_convert_price;

        //强赎触发价
        private BigDecimal force_redeem_price;

        //转债占比(%)
        private BigDecimal convert_amt_ratio;

        //基金持仓
        private String fund_rt;

        //到期时间
        private String short_maturity_dt;

        //剩余年限
        private String year_left;

        //剩余规模(亿元)
        private BigDecimal curr_iss_amt;

        //成交额(万元)
        private BigDecimal volume;

        //
        private String svolume;

        //换手率(%)
        private BigDecimal turnover_rt;

        //到期税前收益(%)
        private BigDecimal ytm_rt;

        //
        private String put_ytm_rt;

        //
        private String notes;

        //
        private String noted;

        //
        private String bond_nm_tip;

        //
        private String redeem_icon;

        //
        private String last_time;

        //
        private String qstatus;

        //
        private String margin_flg;

        //
        private String sqflag;

        //
        private String pb_flag;

        //
        private String adj_cnt;

        //
        private String adj_scnt;

        //
        private String convert_price_valid;

        //转股提示
        private String convert_price_tips;

        //
        private String convert_cd_tip;

        //
        private String ref_yield_info;

        //
        private String adjusted;

        //
        private String orig_iss_amt;

        //
        private String price_tips;

        //
        private String redeem_dt;

        //
        private String real_force_redeem_price;

        //
        private String option_tip;

        //
        private String volatility_rate;
    }
}