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
 * 可转债定投计划表实例类
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
@Data
@TableName("tb_convertible_bond_fixed_investment_plan")
public class ConvertibleBondFixedInvestmentPlan implements Serializable {

    /**
     * 定投计划状态 0=未开始 1=进行中 2=已清仓 3=已完成
     */
    public static final int STATUS_NOT_STARTED = 0;
    public static final int STATUS_IN_PROGRESS = 1;
    public static final int STATUS_CLEARANCE = 2;
    public static final int STATUS_COMPLETED = 3;

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer memberId;

    /**
     * 可转债id(有多个值时以逗号分隔)
     */
    @TableField("convertible_bond_id_comma")
    private String convertibleBondIdComma;

    /**
     * 操作类型 0=多可转债混合定投 1=单可转债定投
     */
    @TableField("type")
    private Integer type;

    /**
     * 定投金额(元)
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 定投周期(月)
     */
    @TableField("cycle")
    private Integer cycle;

    /**
     * 开始时间
     */
    @TableField("start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private Date endTime;

    /**
     * 期望收益率(百分比)
     */
    @TableField("expected_return_rate")
    private BigDecimal expectedReturnRate;

    /**
     * 定投计划状态 0=未开始 1=进行中 2=已清仓 3=已完成
     */
    @TableField("status")
    private Integer status;

    /**
     * 实际收益率(百分比)
     */
    @TableField("actual_return_rate")
    private BigDecimal actualReturnRate;

    /**
     * 收益(元)
     */
    @TableField("income")
    private BigDecimal income;

    /**
     * 备注/定投方法
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
        return "ConvertibleBondFixedInvestmentPlan{" +
                "id=" + id +
                ", convertibleBondIdComma=" + convertibleBondIdComma + '\'' +
                ", type=" + type +
                ", amount=" + amount +
                ", cycle=" + cycle +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", expectedReturnRate=" + expectedReturnRate +
                ", status=" + status +
                ", actualReturnRate=" + actualReturnRate +
                ", income=" + income +
                ", remarks=" + remarks + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
