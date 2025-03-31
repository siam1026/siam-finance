package com.siam.system.modular.mod_finance.controller.member;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicResult;
import com.siam.system.modular.mod_finance.model.param.CapitalRecordParam;
import com.siam.system.modular.mod_finance.model.vo.CapitalStatisticsVo;
import com.siam.system.modular.mod_finance.service.CapitalRecordService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/rest/member/capitalRecord")
@Transactional(rollbackFor = Exception.class)
@Api(tags = "资金收入与支出记录模块相关接口", description = "CapitalRecordController")
public class CapitalRecordController {

    @Autowired
    private CapitalRecordService capitalRecordService;

    /**
     * 资金收入与支出记录列表
     *
     * @author JiangP
     */
    @PostMapping(value = "/list")
    public BasicResult listCapitalRecord(@RequestBody @Validated(value = {}) CapitalRecordParam param) {
        Page page = capitalRecordService.getListByPage(param);
        return BasicResult.success(page);
    }

    /**
     * 添加资金收入与支出记录
     *
     * @author JiangP
     */
    @PostMapping(value = "/insert")
    public BasicResult insertCapitalRecord(@RequestBody @Validated(value = {}) CapitalRecordParam param) {
        capitalRecordService.insert(param);
        return BasicResult.success();
    }

    /**
     * 修改资金收入与支出记录
     *
     * @author JiangP
     */
    @PostMapping(value = "/update")
    public BasicResult updateCapitalRecord(@RequestBody @Validated(value = {}) CapitalRecordParam param) {
        capitalRecordService.update(param);
        return BasicResult.success();
    }

    /*@ApiOperation(value = "删除资金收入与支出记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "资金收入与支出记录表主键id", required = true, paramType = "query", dataType = "int"),
    })
    @PostMapping(value = "/delete")
    public BasicResult deleteCapitalRecord(CapitalRecord capitalRecord){
        BasicResult basicResult = new BasicResult();

        CapitalRecord dbCapitalRecord = capitalRecordService.detail(capitalRecord.getId());
        if(dbCapitalRecord == null){
            basicResult.setSuccess(false);
            basicResult.setCode(BaseCode.ERR);
            basicResult.setMessage("该资金收入与支出记录不存在，删除失败");
            return basicResult;
        }

        //删除资金收入与支出记录
        capitalRecord.setUpdateTime(new Date());
        capitalRecordService.delete(capitalRecord.getId());

        basicResult.setSuccess(true);
        basicResult.setCode(BaseCode.SUCCESS);
        basicResult.setMessage("修改成功");
        return basicResult;
    }*/

    /**
     * 查询资金统计数据-柱状图
     *
     * @author JiangP
     */
    @PostMapping(value = "/statisticsOfHistogramChart")
    public BasicResult statisticsOfHistogramChart(@RequestBody @Validated(value = {}) CapitalRecordParam param) {
        CapitalStatisticsVo statisticsVo = capitalRecordService.statisticsOfHistogramChart(param);
        return BasicResult.success(statisticsVo);
    }

    /**
     * 查询资金统计数据-饼图
     *
     * @author JiangP
     */
    @PostMapping(value = "/statisticsOfPieChart")
    public BasicResult statisticsOfPieChart(@RequestBody @Validated(value = {}) CapitalRecordParam param) {
        CapitalStatisticsVo statisticsVo = capitalRecordService.statisticsOfPieChart(param);
        return BasicResult.success(statisticsVo);
    }

    /**
     * 查询资金统计数据-折线图
     * PS；一次性将所有数据全部查出来，而不是每次选择时间段时来实时请求数据
     */
    @PostMapping(value = "/statisticOfLineChart")
    public BasicResult statisticOfLineChart(@RequestBody @Validated(value = {}) CapitalRecordParam param) {
        Map map = capitalRecordService.statisticOfLineChart(param);
        return BasicResult.success(map);
    }

    /**
     * 统计资金收入与支出金额
     *
     * @author JiangP
     */
    @PostMapping(value = "/statistics")
    public BasicResult statistics(@RequestBody @Validated(value = {}) CapitalRecordParam param) {
        Map map = capitalRecordService.statistics(param);
        return BasicResult.success(map);
    }
}