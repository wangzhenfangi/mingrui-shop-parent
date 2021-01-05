package com.baidu.shop.servce.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.mapper.SpuMapper;
import com.baidu.shop.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 2 *@ClassName GoodsServiceImpl
 * 3 *@Description: TODO
 * 4 *@Author 王振方
 * 5 *@Date 2021/1/5
 *
 * @Version V1.0
 * 7
 **/
@RestController
public class GoodsServiceImpl extends BaseApiService implements GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Override
    public Result<List<SpuDTO>> getSpuInfo(SpuDTO spuDTO) {

        return null;
    }


}
