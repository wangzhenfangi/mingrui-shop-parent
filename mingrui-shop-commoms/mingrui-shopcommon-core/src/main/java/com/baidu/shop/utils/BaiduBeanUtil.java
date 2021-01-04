package com.baidu.shop.utils;

import org.springframework.beans.BeanUtils;

/**
 * 2 *@ClassName BaiduBeanUtil
 * 3 *@Description: TODO
 * 4 *@Author 王振方
 * 5 *@Date 2020/12/25
 *
 * @Version V1.0
 * 7
 **/
public class BaiduBeanUtil {

    public  static <T> T copyProperties(Object source,Class<T> clazz){

        try {
            T t = clazz.newInstance();//創建當前類型的實例
            BeanUtils.copyProperties(source,t);
            return t;
        }catch (InstantiationException e){
            e.printStackTrace();
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }
            return null;

    }

}
