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
 * 股票信息表实例类
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
@Data
@TableName("tb_stock")
public class Stock implements Serializable {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 股票名称
     */
    @TableField("name")
    private String name;

    /**
     * 股票代码
     */
    @TableField("code")
    private String code;

    /**
     * 最新价格
     */
    @TableField("lastest_price")
    private BigDecimal lastestPrice;

    /**
     * 最新涨跌幅(%)
     */
    @TableField("lastest_increase_rate")
    private BigDecimal lastestIncreaseRate;

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
     * 市场简写。支持 sh、sz、hk
     */
    @TableField("market")
    private String market;

    /**
     * 1为上市，其他下市
     */
    @TableField("state")
    private Integer state;

    /**
     * 流通股本，万股
     */
    @TableField("currcapital")
    private BigDecimal currcapital;

    /**
     * 四季度净利润（亿元）
     */
    @TableField("profit_four")
    private BigDecimal profitFour;

    /**
     * 上市日期
     */
    @TableField("listing_date")
    private Date listingDate;

    /**
     * 总股本，万股
     */
    @TableField("totalcapital")
    private BigDecimal totalcapital;

    /**
     * 每股净资产（元）
     */
    @TableField("mgjzc")
    private BigDecimal mgjzc;

    /**
     * 拼音
     */
    @TableField("pinyin")
    private String pinyin;

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
        return "Stock{" +
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
