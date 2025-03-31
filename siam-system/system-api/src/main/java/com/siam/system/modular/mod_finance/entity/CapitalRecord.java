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
@TableName("tb_capital_record")
@ApiModel(value = "资金收入与支出记录表")
public class CapitalRecord {

    @ApiModelProperty(notes = "主键id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer memberId;

    @ApiModelProperty(notes = "资金分类id")
    private Integer capitalTypeId;

    @ApiModelProperty(notes = "交易分类id")
    private Long transactionCategoryId;

    @ApiModelProperty(notes = "操作类型 0=收入 1=支出 2=借入 3=借出 4=待转收入 5=待转支出 6=借入还款 7=借出归还")
    private Integer type;

    @ApiModelProperty(notes = "名称")
    private String name;

    @ApiModelProperty(notes = "金额(元)")
    private BigDecimal amount;

    @ApiModelProperty(notes = "备注")
    private String remarks;

    @ApiModelProperty(notes = "操作关联的收支记录id 默认为0=无")
    private Integer relatedRecordId;

    @ApiModelProperty(notes = "状态 0=无 1=部分还款 2=部分归还 3=部分收入 4=部分支出 5=已还款 6=已归还 7=已收入 8=已支出")
    private Integer status;

    @ApiModelProperty(notes = "创建时间")
    private Date createTime;

    @ApiModelProperty(notes = "修改时间")
    private Date updateTime;
}