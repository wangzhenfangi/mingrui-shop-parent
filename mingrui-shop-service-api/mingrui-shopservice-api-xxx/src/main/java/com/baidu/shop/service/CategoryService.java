package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.validate.group.MingruiOperation;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags="商品分類接口")
public interface CategoryService {

    @ApiOperation(value = "通过查询商品分类")
    @GetMapping(value = "category/list")
    Result<List<CategoryEntity>> getCategoryByPid(Integer pid);

    @ApiOperation(value = "通过品牌id查询商品分类")
    @GetMapping(value = "category/brand")
    Result<List<CategoryEntity>> getCategoryBrandByPid(Integer brandId);


    @ApiOperation(value="通過id刪除分類")
    @DeleteMapping(value="category/del")
    public Result<JsonObject> delCategory(Integer id);

    @ApiOperation(value="通過前臺傳來的string類型的json修改分類")
    //声明哪个组下面的参数参加校验-->当前是校验Update组
    @PutMapping(value="category/update")
    public Result<JsonObject> editCategory(@Validated({MingruiOperation.Update.class}) @RequestBody CategoryEntity entity);

    @ApiOperation(value="通過前臺傳來的數據修改實體類")
    @PostMapping(value = "category/add")
    public Result<JsonObject> addCategory(@Validated({MingruiOperation.Add.class}) @RequestBody CategoryEntity entity);



}



