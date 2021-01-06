package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.entity.SpuEntity;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 2 *@ClassName GoodsService
 * 3 *@Description: TODO
 * 4 *@Author 王振方
 * 5 *@Date 2021/1/5
 *
 * @Version V1.0
 * 7
 **/
@Api(tags = "商品接口")
public interface GoodsService {
    @ApiOperation(value = "获取spu信息")
    @GetMapping(value = "goods/getSpuInfo")
    public Result<PageInfo<SpuEntity>> getSpuInfo(SpuDTO spuDTO);

}
