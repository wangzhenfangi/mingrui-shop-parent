package com.baidu.feign;

import com.baidu.shop.service.GoodsService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 2 *@ClassName GoodsFeign
 * 3 *@Description: TODO
 * 4 *@Author 王振方
 * 5 *@Date 2021/3/4
 *
 * @Version V1.0
 * 7
 **/

@FeignClient(value = "xxx-server",contextId = "GoodsService")
public interface GoodsFeign extends GoodsService {



}
