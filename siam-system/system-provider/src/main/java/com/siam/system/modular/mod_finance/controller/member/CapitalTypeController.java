package com.siam.system.modular.mod_finance.controller.member;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.constant.BaseCode;
import com.siam.system.modular.mod_finance.entity.CapitalType;
import com.siam.system.modular.mod_finance.model.param.CapitalTypeParam;
import com.siam.system.modular.mod_finance.service.CapitalTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(value = "/rest/member/capitalType")
@Transactional(rollbackFor = Exception.class)
@Api(tags = "资金分类模块相关接口", description = "CapitalTypeController")
public class CapitalTypeController {

    @Autowired
    private CapitalTypeService capitalTypeService;

    @ApiOperation(value = "")

    /**
     * 资金分类列表
     *
     * @author JiangP
     */
    @PostMapping(value = "/list")
    public BasicResult listCapitalType(@RequestBody @Validated(value = {}) CapitalTypeParam param) {
        BasicData basicResult = new BasicData();

        Page page = capitalTypeService.getListByPage(param.getPageNo(), param.getPageSize(), param);

        basicResult.setData(page);
        basicResult.setSuccess(true);
        basicResult.setCode(BaseCode.SUCCESS);
        basicResult.setMessage("查询成功");
        return basicResult;
    }

    /**
     * 添加资金分类
     *
     * @author JiangP
     */
    @PostMapping(value = "/insert")
    public BasicResult insertCapitalType(@RequestBody @Validated(value = {}) CapitalTypeParam param) {
        BasicResult basicResult = new BasicResult();

        //添加资金分类
        param.setCreateTime(new Date());
        param.setUpdateTime(new Date());
        capitalTypeService.insertSelective(param);

        basicResult.setSuccess(true);
        basicResult.setCode(BaseCode.SUCCESS);
        basicResult.setMessage("添加成功");
        return basicResult;
    }

    /**
     * 修改资金分类
     *
     * @author JiangP
     */
    @PostMapping(value = "/update")
    public BasicResult updateCapitalType(@RequestBody @Validated(value = {}) CapitalTypeParam param) {
        BasicResult basicResult = new BasicResult();

        CapitalType dbCapitalType = capitalTypeService.selectByPrimaryKey(param.getId());
        if(dbCapitalType == null){
            basicResult.setSuccess(false);
            basicResult.setCode(BaseCode.ERR);
            basicResult.setMessage("该资金分类不存在，修改失败");
            return basicResult;
        }

        //修改资金分类
        param.setUpdateTime(new Date());
        capitalTypeService.updateByPrimaryKeySelective(param);

        basicResult.setSuccess(true);
        basicResult.setCode(BaseCode.SUCCESS);
        basicResult.setMessage("修改成功");
        return basicResult;
    }

    /*@ApiOperation(value = "删除资金分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "资金分类表主键id", required = true, paramType = "query", dataType = "int"),
    })
    @PostMapping(value = "/delete")
    public BasicResult deleteCapitalType(CapitalType capitalType){
        BasicResult basicResult = new BasicResult();

        CapitalType dbCapitalType = capitalTypeService.detail(capitalType.getId());
        if(dbCapitalType == null){
            basicResult.setSuccess(false);
            basicResult.setCode(BaseCode.ERR);
            basicResult.setMessage("该资金分类不存在，删除失败");
            return basicResult;
        }

        //删除资金分类
        capitalType.setUpdateTime(new Date());
        capitalTypeService.delete(capitalType.getId());

        basicResult.setSuccess(true);
        basicResult.setCode(BaseCode.SUCCESS);
        basicResult.setMessage("修改成功");
        return basicResult;
    }*/
}