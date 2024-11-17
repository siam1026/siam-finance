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
@TableName("tb_gold")
@ApiModel(value = "黄金信息表")
public class Gold {

    @ApiModelProperty(notes = "主键id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(notes = "黄金名称")
    private String name;

    @ApiModelProperty(notes = "最新金价(元)")
    private BigDecimal lastestPrice;

    @ApiModelProperty(notes = "最新金价对应的日期")
    private Date lastestPriceDate;

    @ApiModelProperty(notes = "备注")
    private String remarks;

    @ApiModelProperty(notes = "创建时间")
    private Date createTime;

    @ApiModelProperty(notes = "修改时间")
    private Date updateTime;
}