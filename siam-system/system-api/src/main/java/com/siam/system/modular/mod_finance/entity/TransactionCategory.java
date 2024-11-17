package com.siam.system.modular.mod_finance.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 交易分类表实例类
 *
 * @author JiangP
 * @date 2022/10/04 13:46
 */
@Data
@TableName("tb_transaction_category")
public class TransactionCategory implements Serializable {

    /**
     * 主键id
     */
    @TableId("id")
    private Long id;

    private Integer memberId;

    /**
     * 父级id
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 分类名称
     */
    @TableField("name")
    private String name;

    /**
     * 类型 0=收入 1=支出 2=本人资金往来
     */
    private Integer type;

    /**
     * 排序号
     */
    @TableField("sort_number")
    private Integer sortNumber;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改日期
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;
}