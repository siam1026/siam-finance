package com.siam.system.modular.mod_finance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("tb_fund")
@ApiModel(value = "基金信息表")
public class Fund {

    @ApiModelProperty(notes = "主键id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(notes = "基金名称")
    private String name;

    @ApiModelProperty(notes = "基金代码")
    private String code;

    @ApiModelProperty(notes = "最新净值")
    private BigDecimal lastestNetValue;

    /**
     * 最新涨跌幅(%)
     */
    @TableField("lastest_increase_rate")
    private BigDecimal lastestIncreaseRate;

    @ApiModelProperty(notes = "最新净值对应的日期")
    private Date lastestNetValueDate;

    @ApiModelProperty(notes = "备注")
    private String remarks;

    /**
     * 是否为自选 0=否 1=是
     */
    @TableField("is_optional")
    private Boolean isOptional;

    @ApiModelProperty(notes = "创建时间")
    private Date createTime;

    @ApiModelProperty(notes = "修改时间")
    private Date updateTime;
}