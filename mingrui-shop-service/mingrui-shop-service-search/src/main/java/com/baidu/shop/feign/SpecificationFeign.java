package com.baidu.shop.feign;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SpecGroupDTO;
import com.baidu.shop.dto.SpecParamDTO;
import com.baidu.shop.entity.SpecGroupEntity;
import com.baidu.shop.entity.SpecParamEntity;
import com.baidu.shop.service.SpecificationService;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

/**
 * 2 *@ClassName SpecificationFeign
 * 3 *@Description: TODO
 * 4 *@Author 王振方
 * 5 *@Date 2021/3/4
 *
 * @Version V1.0
 * 7
 **/
@FeignClient(value = "xxx-server",contextId = "SpecificationService")
public interface SpecificationFeign extends SpecificationService {

}
