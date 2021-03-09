package com.baidu.controller;

import com.baidu.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * 2 *@ClassName PageController
 * 3 *@Description: TODO
 * 4 *@Author 王振方
 * 5 *@Date 2021/3/8
 *
 * @Version V1.0
 * 7
 **/

//@Controller
//@RequestMapping(value = "item")
public class PageController {

    //@Autowired
    private PageService pageService;

   // @GetMapping(value = "{spuId}.html")
    public String test(@PathVariable(value = "spuId") Integer spuId,ModelMap map){

          Map<String,Object> mapp = pageService.getGoodsInfo(spuId);
          map.putAll(mapp);

        return "item";
    }


}
