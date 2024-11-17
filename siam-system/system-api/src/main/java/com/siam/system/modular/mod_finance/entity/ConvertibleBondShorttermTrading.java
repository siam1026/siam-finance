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
 * 可转债短期交易/做T交易记录表(一个做T买入记录可能对应多个做T卖出操作)实例类
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
@Data
@TableName("tb_convertible_bond_shortterm_trading")
public class ConvertibleBondShorttermTrading implements Serializable {

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
     * 可转债id
     */
    @TableField("convertible_bond_id")
    private Integer convertibleBondId;

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
     * 数量(张)
     */
    @TableField("quantity")
    private BigDecimal quantity;

    /**
     * 价格
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 做T卖出操作关联的做T买入交易id
     */
    @TableField("related_trading_id")
    private Integer relatedTradingId;

    /**
     * 做T状态 0=无 1=进行中 2=已完成
     */
    @TableField("status")
    private Integer status;

    /**
     * 做T卖出操作所获收益(元)
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
        return "ConvertibleBondShorttermTrading{" +
                "id=" + id +
                ", convertibleBondId=" + convertibleBondId +
                ", type=" + type +
                ", amount=" + amount +
                ", serviceCharge=" + serviceCharge +
                ", quantity=" + quantity +
                ", price=" + price +
                ", relatedTradingId=" + relatedTradingId +
                ", status=" + status +
                ", income=" + income +
                ", returnRate=" + returnRate +
                ", remarks=" + remarks + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
