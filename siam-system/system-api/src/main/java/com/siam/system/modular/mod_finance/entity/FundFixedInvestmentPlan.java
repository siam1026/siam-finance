package com.siam.system.modular.mod_finance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("tb_fund_fixed_investment_plan")
@ApiModel(value = "基金定投计划表")
public class FundFixedInvestmentPlan {

    /**
     * 定投计划状态 0=未开始 1=进行中 2=已清仓 3=已完成
     */
    public static final int STATUS_NOT_STARTED = 0;
    public static final int STATUS_IN_PROGRESS = 1;
    public static final int STATUS_CLEARANCE = 2;
    public static final int STATUS_COMPLETED = 3;

    @ApiModelProperty(notes = "主键id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer memberId;

    @ApiModelProperty(notes = "基金id(有多个值时以逗号分隔)")
    private String fundIdComma;

    @ApiModelProperty(notes = "操作类型 0=多基金混合定投 1=单基金定投")
    private Integer type;

    @ApiModelProperty(notes = "定投金额(元)")
    private BigDecimal amount;

    @ApiModelProperty(notes = "定投周期(月)")
    private Integer cycle;

    @ApiModelProperty(notes = "开始时间")
    private Date startTime;

    @ApiModelProperty(notes = "结束时间")
    private Date endTime;

    @ApiModelProperty(notes = "期望收益率(百分比)")
    private BigDecimal expectedReturnRate;

    @ApiModelProperty(notes = "定投计划状态 0=未开始 1=进行中 2=已结束待卖出 3=已完成")
    private Integer status;

    @ApiModelProperty(notes = "实际收益率(百分比)")
    private BigDecimal actualReturnRate;

    @ApiModelProperty(notes = "收益(元)")
    private BigDecimal income;

    @ApiModelProperty(notes = "备注/定投方法")
    private String remarks;

    @ApiModelProperty(notes = "创建时间")
    private Date createTime;

    @ApiModelProperty(notes = "修改时间")
    private Date updateTime;
}