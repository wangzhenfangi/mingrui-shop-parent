package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SpecGroupDTO;
import com.baidu.shop.dto.SpecParamDTO;
import com.baidu.shop.entity.SpecGroupEntity;
import com.baidu.shop.entity.SpecParamEntity;
import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "规格接口")
public interface SpecificationService {

    @ApiOperation(value = "通过条件查询规格组")
    @GetMapping(value = "specgroup/getSpecGroupInfo")
    Result<List<SpecGroupEntity>> getSepcGroupInfo(@SpringQueryMap SpecGroupDTO specGroupDTO);

    @ApiOperation(value = "新增规格组")
    @PostMapping("specgroup/save")
    Result<JSONObject> addGroupInfo(@Validated({MingruiOperation.Add.class}) @RequestBody SpecGroupDTO specGroupDTO);

    @ApiOperation(value = "修改规格组")
    @PutMapping("specgroup/save")
    Result<JSONObject> editGroupInfo(@Validated({MingruiOperation.Update.class}) @RequestBody SpecGroupDTO specGroupDTO);

    @ApiOperation(value = "删除规格组")
    @DeleteMapping(value = "specgroup/delete")
    Result<JSONObject> deleteGroupInfo(Integer id);

    @ApiOperation(value = "查询规格参数")
    @GetMapping(value = "specparam/getSpecParamInfo")
    public Result<List<SpecParamEntity>> getSpecParamInfo(@SpringQueryMap SpecParamDTO specParamDTO);

    @ApiOperation(value = "新增规格参数")
    @PostMapping(value = "specparam/save")
    Result<JSONObject> addParam(@Validated({MingruiOperation.Add.class}) @RequestBody SpecParamDTO specParamDTO);
    @ApiOperation(value = "修改规格参数")
    @PutMapping(value = "specparam/save")
    Result<JSONObject> editParam(@Validated({MingruiOperation.Update.class}) @RequestBody SpecParamDTO specParamDTO);
    @ApiOperation(value = "删除规格参数")
    @DeleteMapping(value = "specparam/del")
    Result<JSONObject> delParam(Integer id);


}
