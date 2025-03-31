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
 * 基础数据设置表实例类
 *
 * @author JiangP
 * @date 2022/01/23 22:04
 */
@Data
@TableName("tb_setting")
public class Setting implements Serializable {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer memberId;

    /**
     * 基金-短期仓金额(元)
     */
    @TableField("fund_short_term_amount")
    private BigDecimal fundShortTermAmount;

    /**
     * 基金-长期仓金额(元)
     */
    @TableField("fund_long_term_amount")
    private BigDecimal fundLongTermAmount;

    /**
     * 股票-短期仓金额(元)
     */
    @TableField("stock_short_term_amount")
    private BigDecimal stockShortTermAmount;

    /**
     * 股票-长期仓金额(元)
     */
    @TableField("stock_long_term_amount")
    private BigDecimal stockLongTermAmount;

    /**
     * 可转债-短期仓金额(元)
     */
    @TableField("convertible_bond_short_term_amount")
    private BigDecimal convertibleBondShortTermAmount;

    /**
     * 可转债-长期仓金额(元)
     */
    @TableField("convertible_bond_long_term_amount")
    private BigDecimal convertibleBondLongTermAmount;

    /**
     * 需推送股价信息的邮箱地址(多个以逗号分隔)
     */
    @TableField("push_stock_price_email_address")
    private String pushStockPriceEmailAddress;

    /**
     * 公积金余额(元)
     */
    @TableField("accumulation_fund_balance")
    private BigDecimal accumulationFundBalance;

    /**
     * 医保余额(元)
     */
    @TableField("medical_insurance_balance")
    private BigDecimal medicalInsuranceBalance;

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
        return "Setting{" +
                "id=" + id +
                ", fundShortTermAmount=" + fundShortTermAmount +
                ", fundLongTermAmount=" + fundLongTermAmount +
                ", stockShortTermAmount=" + stockShortTermAmount +
                ", stockLongTermAmount=" + stockLongTermAmount +
                ", convertibleBondShortTermAmount=" + convertibleBondShortTermAmount +
                ", convertibleBondLongTermAmount=" + convertibleBondLongTermAmount +
                ", pushStockPriceEmailAddress=" + pushStockPriceEmailAddress + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
