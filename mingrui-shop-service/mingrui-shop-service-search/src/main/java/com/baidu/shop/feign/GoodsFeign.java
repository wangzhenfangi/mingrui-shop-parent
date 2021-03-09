package com.baidu.shop.feign;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SkuDTO;
import com.baidu.shop.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

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
