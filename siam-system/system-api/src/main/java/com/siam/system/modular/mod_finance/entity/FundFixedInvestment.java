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
 * 基金定投交易记录表实例类
 *
 * @author JiangP
 * @date 2021/11/08 14:42
 */
@Data
@TableName("tb_fund_fixed_investment")
public class FundFixedInvestment implements Serializable {

    /**
     * 操作类型 0=买入 1=卖出
     */
    public static final int TYPE_BUY = 0;
    public static final int TYPE_SELL = 1;

    /**
     * 做T状态 0=无 1=进行中 2=已完成
     */
    public static final int STATUS_NOTHING = 0;
    public static final int STATUS_IN_PROGRESS = 1;
    public static final int STATUS_COMPLETED = 2;

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer memberId;

    /**
     * 基金定投计划id
     */
    @TableField("plan_id")
    private Integer planId;

    /**
     * 基金id
     */
    @TableField("fund_id")
    private Integer fundId;

    /**
     * 操作类型 0=买入 1=卖出
     */
    @TableField("type")
    private Integer type;

    /**
     * 金额(元)
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 手续费(元)
     */
    @TableField("service_charge")
    private BigDecimal serviceCharge;

    /**
     * 份额(份)
     */
    @TableField("shares")
    private BigDecimal shares;

    /**
     * 净值
     */
    @TableField("net_value")
    private BigDecimal netValue;

    /**
     * 定投结束卖出操作所获收益(元)
     */
    @TableField("income")
    private BigDecimal income;

    /**
     * 收益率(百分比)
     */
    @TableField("return_rate")
    private BigDecimal returnRate;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 买入操作关联的卖出交易id
     */
    @TableField("related_trading_id")
    private Integer relatedTradingId;

    /**
     * 状态 0=无 1=进行中 2=已完成
     */
    @TableField("status")
    private Integer status;

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
}