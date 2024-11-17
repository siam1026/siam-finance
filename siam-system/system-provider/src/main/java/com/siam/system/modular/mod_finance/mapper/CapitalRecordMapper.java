package com.siam.system.modular.mod_finance.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.system.modular.mod_finance.entity.CapitalRecord;
import com.siam.system.modular.mod_finance.model.example.CapitalRecordExample;
import com.siam.system.modular.mod_finance.model.param.CapitalRecordParam;
import com.siam.system.modular.mod_finance.model.result.CapitalRecordResult;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CapitalRecordMapper {
    int countByExample(CapitalRecordExample example);

    int deleteByExample(CapitalRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CapitalRecord record);

    int insertSelective(CapitalRecord record);

    List<CapitalRecord> selectByExample(CapitalRecordExample example);

    CapitalRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CapitalRecord record, @Param("example") CapitalRecordExample example);

    int updateByExample(@Param("record") CapitalRecord record, @Param("example") CapitalRecordExample example);

    int updateByPrimaryKeySelective(CapitalRecord record);

    int updateByPrimaryKey(CapitalRecord record);

    @ResultMap("CustomResultMap")
    @Select("<script>select cr.*, b.name as transactionCategoryName from tb_capital_record cr left join tb_transaction_category b on cr.transaction_category_id = b.id " +
            "<where> 1=1 " +
            "<if test=\"capitalRecord.id != null\"> AND cr.id = #{capitalRecord.id} </if>" +
            "<if test=\"capitalRecord.memberId != null\"> AND cr.member_id = #{capitalRecord.memberId} </if>" +
            "<if test=\"capitalRecord.capitalTypeId != null\"> AND cr.capital_type_id = #{capitalRecord.capitalTypeId} </if>" +
            "<if test=\"capitalRecord.transactionCategoryId != null\"> AND cr.transaction_category_id = #{capitalRecord.transactionCategoryId} </if>" +
            "<if test=\"capitalRecord.type != null\"> AND cr.type = #{capitalRecord.type} </if>" +
            "<if test=\"capitalRecord.name != null and capitalRecord.name !=''\"> AND cr.name like '%${capitalRecord.name}%' </if>" +
            "<if test=\"capitalRecord.amount != null\"> AND cr.amount = #{capitalRecord.amount} </if>" +
            "<if test=\"capitalRecord.remarks != null and capitalRecord.remarks !=''\"> AND cr.remarks like '%${capitalRecord.remarks}%' </if>" +
            "<if test=\"capitalRecord.relatedRecordId != null\"> AND cr.related_record_id = #{capitalRecord.relatedRecordId} </if>" +
            "<if test=\"capitalRecord.status != null\"> AND cr.status = #{capitalRecord.status} </if>" +
            "<if test=\"capitalRecord.startDate != null and capitalRecord.startDate !=''\"> AND DATE_FORMAT(cr.create_time, '%Y/%m/%d') &gt;= #{capitalRecord.startDate} </if>" +
            "<if test=\"capitalRecord.endDate != null and capitalRecord.endDate !=''\"> AND DATE_FORMAT(cr.create_time, '%Y/%m/%d') &lt;= #{capitalRecord.endDate} </if>" +
            "</where> order by cr.create_time desc" +
            "</script>")
    Page<CapitalRecordResult> getListByPage(@Param("page") Page page, @Param("capitalRecord") CapitalRecordParam capitalRecord);

    @Select("select IFNULL(sum(cr.amount), 0) from tb_capital_record cr where cr.related_record_id = #{relatedRecordId}")
    BigDecimal selectSumAmountByRelatedRecordId(@Param("relatedRecordId") Integer relatedRecordId);

    //统计资金收入/支出金额
    @Select("<script>select IFNULL(sum(cr.amount), 0) from tb_capital_record cr " +
            "<where> 1 = 1 and cr.related_record_id = 0 " +
            "<if test=\"capitalRecord.id != null\"> AND cr.id = #{capitalRecord.id} </if>" +
            "<if test=\"capitalRecord.memberId != null\"> AND cr.member_id = #{capitalRecord.memberId} </if>" +
            "<if test=\"capitalRecord.capitalTypeId != null\"> AND cr.capital_type_id = #{capitalRecord.capitalTypeId} </if>" +
            "<if test=\"capitalRecord.transactionCategoryId != null\"> AND cr.transaction_category_id = #{capitalRecord.transactionCategoryId} </if>" +
            "<if test=\"capitalRecord.type != null\"> AND cr.type = #{capitalRecord.type} </if>" +
            "<if test=\"capitalRecord.name != null and capitalRecord.name !=''\"> AND cr.name like '%${capitalRecord.name}%' </if>" +
            "<if test=\"capitalRecord.amount != null\"> AND cr.amount = #{capitalRecord.amount} </if>" +
            "<if test=\"capitalRecord.remarks != null and capitalRecord.remarks !=''\"> AND cr.remarks like '%${capitalRecord.remarks}%' </if>" +
            "<if test=\"capitalRecord.relatedRecordId != null\"> AND cr.related_record_id = #{capitalRecord.relatedRecordId} </if>" +
            "<if test=\"capitalRecord.status != null\"> AND cr.status = #{capitalRecord.status} </if>" +
            "<if test=\"capitalRecord.startDate != null and capitalRecord.startDate !=''\"> AND DATE_FORMAT(cr.create_time, '%Y/%m/%d') &gt;= #{capitalRecord.startDate} </if>" +
            "<if test=\"capitalRecord.endDate != null and capitalRecord.endDate !=''\"> AND DATE_FORMAT(cr.create_time, '%Y/%m/%d') &lt;= #{capitalRecord.endDate} </if>" +
            "</where>" +
            "</script>")
    BigDecimal sumAmount(@Param("capitalRecord") CapitalRecordParam capitalRecord);

    //统计资金收入/支出金额
    @Select("<script>select IFNULL(sum(cr.amount), 0) from tb_capital_record cr " +
            "<where> 1 = 1 " +
            "<if test=\"capitalRecord.id != null\"> AND cr.id = #{capitalRecord.id} </if>" +
            "<if test=\"capitalRecord.memberId != null\"> AND cr.member_id = #{capitalRecord.memberId} </if>" +
            "<if test=\"capitalRecord.capitalTypeId != null\"> AND cr.capital_type_id = #{capitalRecord.capitalTypeId} </if>" +
            "<if test=\"capitalRecord.transactionCategoryId != null\"> AND cr.transaction_category_id = #{capitalRecord.transactionCategoryId} </if>" +
            "<if test=\"capitalRecord.type != null\"> AND cr.type = #{capitalRecord.type} </if>" +
            "<if test=\"capitalRecord.name != null and capitalRecord.name !=''\"> AND cr.name like '%${capitalRecord.name}%' </if>" +
            "<if test=\"capitalRecord.amount != null\"> AND cr.amount = #{capitalRecord.amount} </if>" +
            "<if test=\"capitalRecord.remarks != null and capitalRecord.remarks !=''\"> AND cr.remarks like '%${capitalRecord.remarks}%' </if>" +
            "<if test=\"capitalRecord.relatedRecordId != null and capitalRecord.relatedRecordId != -1\"> AND cr.related_record_id = #{capitalRecord.relatedRecordId} </if>" +
            "<if test=\"capitalRecord.relatedRecordId != null and capitalRecord.relatedRecordId == -1\"> AND cr.related_record_id != 0 </if>" +
            "<if test=\"capitalRecord.status != null\"> AND cr.status = #{capitalRecord.status} </if>" +
            "<if test=\"capitalRecord.startDate != null and capitalRecord.startDate !=''\"> AND DATE_FORMAT(cr.create_time, '%Y/%m/%d') &gt;= #{capitalRecord.startDate} </if>" +
            "<if test=\"capitalRecord.endDate != null and capitalRecord.endDate !=''\"> AND DATE_FORMAT(cr.create_time, '%Y/%m/%d') &lt;= #{capitalRecord.endDate} </if>" +
            "</where>" +
            "</script>")
    BigDecimal sumAmountAllowRelatedRecordId(@Param("capitalRecord") CapitalRecordParam capitalRecord);

    //按照资金分类统计资金收入/支出金额
    @Select("<script>select b.name as typeName, IFNULL(sum(cr.amount), 0) amount from tb_capital_record cr " +
            "left join tb_capital_type b on cr.capital_type_id = b.id " +
            "<where> 1 = 1 and cr.related_record_id = 0 " +
            "<if test=\"capitalRecord.id != null\"> AND cr.id = #{capitalRecord.id} </if>" +
            "<if test=\"capitalRecord.memberId != null\"> AND cr.member_id = #{capitalRecord.memberId} </if>" +
            "<if test=\"capitalRecord.capitalTypeId != null\"> AND cr.capital_type_id = #{capitalRecord.capitalTypeId} </if>" +
            "<if test=\"capitalRecord.transactionCategoryId != null\"> AND cr.transaction_category_id = #{capitalRecord.transactionCategoryId} </if>" +
            "<if test=\"capitalRecord.type != null\"> AND cr.type = #{capitalRecord.type} </if>" +
            "<if test=\"capitalRecord.name != null and capitalRecord.name !=''\"> AND cr.name like '%${capitalRecord.name}%' </if>" +
            "<if test=\"capitalRecord.amount != null\"> AND cr.amount = #{capitalRecord.amount} </if>" +
            "<if test=\"capitalRecord.remarks != null and capitalRecord.remarks !=''\"> AND cr.remarks like '%${capitalRecord.remarks}%' </if>" +
            "<if test=\"capitalRecord.relatedRecordId != null\"> AND cr.related_record_id = #{capitalRecord.relatedRecordId} </if>" +
            "<if test=\"capitalRecord.status != null\"> AND cr.status = #{capitalRecord.status} </if>" +
            "<if test=\"capitalRecord.startDate != null and capitalRecord.startDate !=''\"> AND DATE_FORMAT(cr.create_time, '%Y/%m/%d') &gt;= #{capitalRecord.startDate} </if>" +
            "<if test=\"capitalRecord.endDate != null and capitalRecord.endDate !=''\"> AND DATE_FORMAT(cr.create_time, '%Y/%m/%d') &lt;= #{capitalRecord.endDate} </if>" +
            "</where> GROUP BY cr.capital_type_id" +
            "</script>")
    List<Map<String, Object>> sumAmountByCapitalType(@Param("capitalRecord") CapitalRecordParam capitalRecord);

    //按照交易分类统计资金收入/支出金额
    @Select("<script>select b.name as typeName, IFNULL(sum(cr.amount), 0) amount from tb_capital_record cr " +
            "left join tb_transaction_category b on cr.transaction_category_id = b.id " +
            "<where> 1 = 1 and cr.related_record_id = 0 " +
            "<if test=\"capitalRecord.id != null\"> AND cr.id = #{capitalRecord.id} </if>" +
            "<if test=\"capitalRecord.memberId != null\"> AND cr.member_id = #{capitalRecord.memberId} </if>" +
            "<if test=\"capitalRecord.capitalTypeId != null and capitalRecord.capitalTypeId != -1\"> AND cr.capital_type_id = #{capitalRecord.capitalTypeId} </if>" +
            "<if test=\"capitalRecord.transactionCategoryId != null\"> AND cr.transaction_category_id = #{capitalRecord.transactionCategoryId} </if>" +
            "<if test=\"capitalRecord.type != null\"> AND cr.type = #{capitalRecord.type} </if>" +
            "<if test=\"capitalRecord.name != null and capitalRecord.name !=''\"> AND cr.name like '%${capitalRecord.name}%' </if>" +
            "<if test=\"capitalRecord.amount != null\"> AND cr.amount = #{capitalRecord.amount} </if>" +
            "<if test=\"capitalRecord.remarks != null and capitalRecord.remarks !=''\"> AND cr.remarks like '%${capitalRecord.remarks}%' </if>" +
            "<if test=\"capitalRecord.relatedRecordId != null\"> AND cr.related_record_id = #{capitalRecord.relatedRecordId} </if>" +
            "<if test=\"capitalRecord.status != null\"> AND cr.status = #{capitalRecord.status} </if>" +
            "<if test=\"capitalRecord.startDate != null and capitalRecord.startDate !=''\"> AND DATE_FORMAT(cr.create_time, '%Y/%m/%d') &gt;= #{capitalRecord.startDate} </if>" +
            "<if test=\"capitalRecord.endDate != null and capitalRecord.endDate !=''\"> AND DATE_FORMAT(cr.create_time, '%Y/%m/%d') &lt;= #{capitalRecord.endDate} </if>" +
            "</where> GROUP BY cr.transaction_category_id" +
            "</script>")
    List<Map<String, Object>> sumAmountByTransactionCategory(@Param("capitalRecord") CapitalRecordParam capitalRecord);

    @Select("<script>SELECT DATE_FORMAT(create_time, '%Y-%m-%d') AS orderDate FROM tb_capital_record " +
            "<where> 1 = 1 and related_record_id = 0 " +
            "<if test=\"param.memberId != null\"> AND member_id = #{param.memberId} </if>" +
            "<if test=\"param.capitalTypeId != null and param.capitalTypeId != -1\"> AND capital_type_id = #{param.capitalTypeId} </if>" +
            "<if test=\"param.capitalTypeId != null and param.capitalTypeId == -1\"> AND capital_type_id in (1, 2, 3) </if>" +
            "<if test=\"param.transactionCategoryId != null\"> AND transaction_category_id = #{param.transactionCategoryId} </if>" +
            "<if test=\"param.type != null\"> AND type = #{param.type} </if>" +
            "</where> order by create_time asc limit 1 " +
            "</script>")
    String selectStartDateOrder(@Param("param") CapitalRecordParam param);

    @Select("<script>SELECT DATE_FORMAT(create_time, '%Y-%m-%d') AS orderDate, COUNT(*) AS orderCount, IFNULL(SUM(amount), 0) AS orderAmount FROM tb_capital_record " +
            "<where> 1 = 1 and related_record_id = 0 " +
            "<if test=\"param.memberId != null\"> AND member_id = #{param.memberId} </if>" +
            "<if test=\"param.capitalTypeId != null and param.capitalTypeId != -1\"> AND capital_type_id = #{param.capitalTypeId} </if>" +
            "<if test=\"param.capitalTypeId != null and param.capitalTypeId == -1\"> AND capital_type_id in (1, 2, 3) </if>" +
            "<if test=\"param.transactionCategoryId != null\"> AND transaction_category_id = #{param.transactionCategoryId} </if>" +
            "<if test=\"param.type != null\"> AND type = #{param.type} </if>" +
            "</where> GROUP BY DATE_FORMAT(create_time, '%Y-%m-%d')" +
            "</script>")
    List<Map<String, Object>> selectStatisticOrder(@Param("param") CapitalRecordParam param);

    @Select("<script>SELECT DATE_FORMAT(create_time, '%Y-%m') AS orderDate, COUNT(*) AS orderCount, IFNULL(SUM(amount), 0) AS orderAmount FROM tb_capital_record " +
            "<where> 1 = 1 and related_record_id = 0 " +
            "<if test=\"param.memberId != null\"> AND member_id = #{param.memberId} </if>" +
            "<if test=\"param.capitalTypeId != null and param.capitalTypeId != -1\"> AND capital_type_id = #{param.capitalTypeId} </if>" +
            "<if test=\"param.capitalTypeId != null and param.capitalTypeId == -1\"> AND capital_type_id in (1, 2, 3) </if>" +
            "<if test=\"param.transactionCategoryId != null\"> AND transaction_category_id = #{param.transactionCategoryId} </if>" +
            "<if test=\"param.type != null\"> AND type = #{param.type} </if>" +
            "</where> GROUP BY DATE_FORMAT(create_time, '%Y-%m')" +
            "</script>")
    List<Map<String, Object>> selectStatisticOrderByMonth(@Param("param") CapitalRecordParam param);

    @Select("<script>SELECT DATE_FORMAT(create_time, '%Y') AS orderDate, COUNT(*) AS orderCount, IFNULL(SUM(amount), 0) AS orderAmount FROM tb_capital_record " +
            "<where> 1 = 1 and related_record_id = 0 " +
            "<if test=\"param.memberId != null\"> AND member_id = #{param.memberId} </if>" +
            "<if test=\"param.capitalTypeId != null and param.capitalTypeId != -1\"> AND capital_type_id = #{param.capitalTypeId} </if>" +
            "<if test=\"param.capitalTypeId != null and param.capitalTypeId == -1\"> AND capital_type_id in (1, 2, 3) </if>" +
            "<if test=\"param.transactionCategoryId != null\"> AND transaction_category_id = #{param.transactionCategoryId} </if>" +
            "<if test=\"param.type != null\"> AND type = #{param.type} </if>" +
            "</where> GROUP BY DATE_FORMAT(create_time, '%Y')" +
            "</script>")
    List<Map<String, Object>> selectStatisticOrderByYear(@Param("param") CapitalRecordParam param);
}