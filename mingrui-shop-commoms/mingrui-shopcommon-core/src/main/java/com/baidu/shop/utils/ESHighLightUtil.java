package com.baidu.shop.utils;

import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.elasticsearch.core.SearchHit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 2 *@ClassName ESHighLightUtil
 * 3 *@Description: TODO
 * 4 *@Author 王振方
 * 5 *@Date 2021/3/4
 *
 * @Version V1.0
 * 7
 **/
public class ESHighLightUtil {
//
//    public static HighlightBuilder getHighlightBuilder(String ...highLightField)
//    {
//        HighlightBuilder highlightBuilder = new HighlightBuilder();
//        Arrays.asList(highLightField).forEach(hlf -> {
//            HighlightBuilder.Field field = new HighlightBuilder.Field(hlf);
//            field.preTags("<span style='color:red'>");
//            field.postTags("</span>");
//            highlightBuilder.field(field);//这个值不会被覆盖,看源码
//        });
//        return highlightBuilder;
//    }
    //将返回的内容替换成高亮
//    public static <T> List<SearchHit<T>> getHighLightHit(List<SearchHit<T>>
//                                                                 list){
//        return list.stream().map(hit -> {
////得到高亮字段
//            Map<String, List<String>> highlightFields =
//                    hit.getHighlightFields();
//            highlightFields.forEach((key,value) -> {
//                try {
//                    T content = hit.getContent();//当前文档 T为当前文档类型
//                //content.getClass()获取当前文档类型,并且得到排序字段的set方法
//                //注意这种首字母大写的方式效率非常低,大数据环境下绝对不允许,但是可以实现效果
//                //可以参考ascll表来实现首字母大写
//                    Method method = content.getClass().getMethod("set" +
//                            String.valueOf(key.charAt(0)).toUpperCase() + key.substring(1),String.class);
//                //执行set方法并赋值
//                    method.invoke(content,value.get(0));
//                } catch (NoSuchMethodException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                } catch (InvocationTargetException e) {
//                    e.printStackTrace();
//                }
//            });
//            return hit;
//        }).collect(Collectors.toList());
//    }
//
//    public static <T> List<T> getHighlightList(List<SearchHit<T>> searchHits){
//
//        return searchHits.stream().map(searchHit -> {
//            T content = searchHit.getContent();
//            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
//            highlightFields.forEach((key, value) -> {
//
//                try {
//                    //不建议使用拼接字符串的方式取首字母大写
//                    //String.valueOf(key.toCharArray()[0]).toUpperCase() + key.substring(1) 这段代码就只是为了让首字母大写
//                    //content.getClass() 得到类的类型
//                    //getMethod()通过方法名得到方法
//                    Method method = content.getClass().getMethod("set" + firstCharUpperCase(key), String.class);
//                    //执行方法
//                    method.invoke(content, value.get(0));
//                } catch (NoSuchMethodException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                } catch (InvocationTargetException e) {
//                    e.printStackTrace();
//                }
//            });
//            return content;
//        }).collect(Collectors.toList());
//    }

    public static HighlightBuilder getHighlightBuilder(String ...field){

        HighlightBuilder highlightBuilder = new HighlightBuilder();

        Arrays.asList(field).stream().forEach(f -> {
            highlightBuilder.field(f);//设置需要高亮的字段
            highlightBuilder.preTags("<span style='color:red'>");//前置标签
            highlightBuilder.postTags("</span>");//后置标签
        });

        return highlightBuilder;
    }

    public static <T> List<T> getHighlightList(List<SearchHit<T>> searchHits){

        return searchHits.stream().map(searchHit -> {
            T content = searchHit.getContent();
            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
            highlightFields.forEach((key, value) -> {

                try {
                    //不建议使用拼接字符串的方式取首字母大写
                    //String.valueOf(key.toCharArray()[0]).toUpperCase() + key.substring(1) 这段代码就只是为了让首字母大写
                    //content.getClass() 得到类的类型
                    //getMethod()通过方法名得到方法
                    Method method = content.getClass().getMethod("set" + firstCharUpper(key), String.class);
                    //执行方法
                    method.invoke(content, value.get(0));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
            return content;
        }).collect(Collectors.toList());
    }
    /**
     * 将字符串首字母大写
     * ascll码表的值(小写英文字母的值) - 32 --> 大写字母的值
     * @param str
     * @return
     */
    public static String firstCharUpper(String str){

        char[] chars = str.toCharArray();
        chars[0] -= 32;
        return String.valueOf(chars);
    }
    //首字母大写,效率最高!
//    private static String firstCharUpperCase(String name){
//        char[] chars = name.toCharArray();
//        chars[0] -= 32;
//        return String.valueOf(chars);
//    }

}
