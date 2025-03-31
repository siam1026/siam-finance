package com.siam.system.modular.mod_finance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 可转债信息表实例类
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
@Data
@TableName("tb_convertible_bond")
public class ConvertibleBond implements Serializable {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer memberId;

    /**
     * 可转债名称
     */
    @TableField("name")
    private String name;

    /**
     * 可转债代码
     */
    @TableField("code")
    private String code;

    /**
     * 最新价格
     */
    @TableField("lastest_price")
    private BigDecimal lastestPrice;

    /**
     * 最新价格对应的日期
     */
    @TableField("lastest_price_date")
    private Date lastestPriceDate;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 拼音
     */
    @TableField("bond_py")
    private String bondPy;

    /**
     * 涨跌幅(%)
     */
    @TableField("increase_rt")
    private BigDecimal increaseRt;

    /**
     * 正股代码
     */
    @TableField("stock_id")
    private String stockId;

    /**
     * 正股名称
     */
    @TableField("stock_nm")
    private String stockNm;

    /**
     * 正股拼音
     */
    @TableField("stock_py")
    private String stockPy;

    /**
     * 正股价
     */
    @TableField("sprice")
    private BigDecimal sprice;

    /**
     * 正股涨跌
     */
    @TableField("sincrease_rt")
    private BigDecimal sincreaseRt;

    /**
     * 正股PB
     */
    @TableField("pb")
    private BigDecimal pb;

    /**
     * 转股价
     */
    @TableField("convert_price")
    private BigDecimal convertPrice;

    /**
     * 转股价值
     */
    @TableField("convert_value")
    private BigDecimal convertValue;

    /**
     * 溢价率
     */
    @TableField("premium_rt")
    private BigDecimal premiumRt;

    /**
     * 债券评级
     */
    @TableField("rating_cd")
    private String ratingCd;

    /**
     * 回售触发价
     */
    @TableField("put_convert_price")
    private BigDecimal putConvertPrice;

    /**
     * 强赎触发价
     */
    @TableField("force_redeem_price")
    private BigDecimal forceRedeemPrice;

    /**
     * 转债占比(%)
     */
    @TableField("convert_amt_ratio")
    private BigDecimal convertAmtRatio;

    /**
     * 基金持仓
     */
    @TableField("fund_rt")
    private String fundRt;

    /**
     * 到期时间
     */
    @TableField("short_maturity_dt")
    private String shortMaturityDt;

    /**
     * 剩余年限
     */
    @TableField("year_left")
    private String yearLeft;

    /**
     * 剩余规模(亿元)
     */
    @TableField("curr_iss_amt")
    private BigDecimal currIssAmt;

    /**
     * 成交额(万元)
     */
    @TableField("volume")
    private BigDecimal volume;

    /**
     * 换手率(%)
     */
    @TableField("turnover_rt")
    private BigDecimal turnoverRt;

    /**
     * 到期税前收益(%)
     */
    @TableField("ytm_rt")
    private BigDecimal ytmRt;

    /**
     * 转股提示
     */
    @TableField("convert_price_tips")
    private String convertPriceTips;

    /**
     * 是否为自选 0=否 1=是
     */
    @TableField("is_optional")
    private Boolean isOptional;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;

    @Override
    public String toString() {
        return "ConvertibleBond{" +
                "id=" + id +
                ", name=" + name + '\'' +
                ", code=" + code + '\'' +
                ", lastestPrice=" + lastestPrice +
                ", lastestPriceDate=" + lastestPriceDate +
                ", remarks=" + remarks + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
