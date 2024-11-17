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
@TableName("tb_fund_shortterm_trading")
@ApiModel(value = "基金短期交易/做T交易记录表")
public class FundShorttermTrading {

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

    @ApiModelProperty(notes = "主键id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer memberId;

    @ApiModelProperty(notes = "基金id")
    private Integer fundId;

    @ApiModelProperty(notes = "操作类型 0=买入 1=卖出")
    private Integer type;

    @ApiModelProperty(notes = "金额(元)")
    private BigDecimal amount;

    @ApiModelProperty(notes = "手续费(元)")
    private BigDecimal serviceCharge;

    @ApiModelProperty(notes = "份额(份)")
    private BigDecimal shares;

    @ApiModelProperty(notes = "净值")
    private BigDecimal netValue;

    @ApiModelProperty(notes = "做T卖出操作关联的做T买入交易id")
    private Integer relatedTradingId;

    @ApiModelProperty(notes = "做T状态 0=无 1=进行中 2=已完成")
    private Integer status;

    @ApiModelProperty(notes = "做T卖出操作所获收益(元)")
    private BigDecimal income;

    @ApiModelProperty(notes = "收益率(百分比)")
    private BigDecimal returnRate;

    @ApiModelProperty(notes = "备注")
    private String remarks;

    @ApiModelProperty(notes = "创建时间")
    private Date createTime;

    @ApiModelProperty(notes = "修改时间")
    private Date updateTime;
}