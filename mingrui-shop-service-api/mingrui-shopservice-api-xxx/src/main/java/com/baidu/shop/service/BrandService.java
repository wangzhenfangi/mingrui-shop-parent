package com.baidu.shop.service;


import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.BrandDTO;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.validate.group.MingruiOperation;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;


import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;

@Api(value = "品牌接口")
public interface BrandService {


    @ApiOperation(value="查詢品牌列表")
    @GetMapping(value = "brand/list")
    Result<PageInfo<BrandEntity>> getBrandInfo(BrandDTO brandDTO);


    @ApiOperation(value = "新增品牌")
    @PostMapping(value = "brand/save")
    public Result<JSONObject> save(@Validated({MingruiOperation.Add.class}) @RequestBody BrandDTO brandDTO);

    @PutMapping(value = "brand/save")
    @ApiOperation(value = "修改品牌")
    Result<JSONObject> editBrandInfo(@RequestBody BrandDTO brandDTO);

    @DeleteMapping(value = "brand/delete")
    @ApiOperation(value = "删除品牌")
    Result<JSONObject> deleteBrandInfo(Integer id);


}
