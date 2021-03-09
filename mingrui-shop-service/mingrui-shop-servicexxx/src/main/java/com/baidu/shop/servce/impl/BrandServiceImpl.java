package com.baidu.shop.servce.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.BrandDTO;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.entity.CategoryBrandEntity;
import com.baidu.shop.mapper.BrandMapper;
import com.baidu.shop.mapper.CategoryBrandMapper;
import com.baidu.shop.service.BrandService;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.ObjectUtil;
import com.baidu.shop.utils.PinyinUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 2 *@ClassName BrandServiceImpl
 * 3 *@Description: TODO
 * 4 *@Author 王振方
 * 5 *@Date 2020/12/25
 *
 * @Version V1.0
 * 7
 **/
@RestController
public class BrandServiceImpl extends BaseApiService implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryBrandMapper categoryBrandMapper;

    @Override
    public Result<List<BrandEntity>> getBrandByIdList(String ids) {

        List<Integer> idList = Arrays.asList(ids.split(",")).stream().map(idStr -> Integer.valueOf(idStr)).collect(Collectors.toList());
        List<BrandEntity> brandEntities = brandMapper.selectByIdList(idList);
        return this.setResultSuccess(brandEntities);
    }


    //修改品牌
    @Transactional
    @Override
    public Result<JSONObject> editBrandInfo(BrandDTO brandDTO) {

        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO, BrandEntity.class);
        brandEntity.setLetter(PinyinUtil.getUpperCase(String.valueOf(brandEntity.getName().toCharArray()[0]), false).toCharArray()[0]);
        brandMapper.updateByPrimaryKeySelective(brandEntity);

        //先通过brandId删除中间表的数据
        Example example = new Example(CategoryBrandEntity.class);
        example.createCriteria().andEqualTo("brandId",brandEntity.getId());
        categoryBrandMapper.deleteByExample(example);
//        this.deleteCategoryBrandByBrandId(brandEntity.getId());
        //批量新增 / 新增
        String categories = brandDTO.getCategories();//得到分类集合字符串
        if(StringUtils.isEmpty(brandDTO.getCategories())) return this.setResultError("");

        //判断分类集合字符串中是否包含,
        if(categories.contains(",")){//多个分类 --> 批量新增

            categoryBrandMapper.insertList(
                    Arrays.asList(categories.split(","))
                            .stream()
                            .map(categoryIdStr -> new CategoryBrandEntity(Integer.valueOf(categoryIdStr)
                                    ,brandEntity.getId()))
                            .collect(Collectors.toList())
            );

        }else{//普通单个新增
            this.insertCategoryBrandList(brandDTO.getCategories(),brandEntity.getId());

            CategoryBrandEntity categoryBrandEntity = new CategoryBrandEntity();
            categoryBrandEntity.setBrandId(brandEntity.getId());
            categoryBrandEntity.setCategoryId(Integer.valueOf(categories));

            categoryBrandMapper.insertSelective(categoryBrandEntity);
        }
        return this.setResultSuccess();
    }

    //刪除品牌
    @Transactional
    @Override
    public Result<JSONObject> deleteBrandInfo(Integer id) {
        //
        brandMapper.deleteByPrimaryKey(id);
        //删除品牌关联的分类的数据
        this.deleteCategoryBrandByBrandId(id);

        return this.setResultSuccess();
    }



    @Transactional
    @Override
    public Result<JSONObject> save(BrandDTO brandDTO) {
        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO, BrandEntity.class);

        //brandDTO.setLetter(PinyinUtil.getUpperCase(String.valueOf(brandDTO.getName().charAt(0)),PinyinUtil.TO_FIRST_CHAR_PINYIN).charAt(0));
        brandEntity.setLetter(PinyinUtil.getUpperCase(String.valueOf(brandEntity.getName().toCharArray()[0]), false).toCharArray()[0]);
        brandMapper.insertSelective(brandEntity);


        String categories = brandDTO.getCategories();//得到分类集合字符串
        if(StringUtils.isEmpty(brandDTO.getCategories())) return this.setResultError("分类信息不能为空");
        List<CategoryBrandEntity> categoryBrandEntities = new ArrayList<>();

        //判断分类集合字符串中是否包含,
        if(categories.contains(",")){//多个分类 --> 批量新增
            String[] categoryArr = categories.split(",");
            for (String s : categoryArr) {
                CategoryBrandEntity categoryBrandEntity = new CategoryBrandEntity();
                categoryBrandEntity.setBrandId(brandEntity.getId());
                categoryBrandEntity.setCategoryId(Integer.valueOf(s));
                categoryBrandEntities.add(categoryBrandEntity);
            }
            categoryBrandMapper.insertList(categoryBrandEntities);
        }else{//普通单个新增

            CategoryBrandEntity categoryBrandEntity = new CategoryBrandEntity();
            categoryBrandEntity.setBrandId(brandEntity.getId());
            categoryBrandEntity.setCategoryId(Integer.valueOf(categories));

            categoryBrandMapper.insertSelective(categoryBrandEntity);
        }

        return this.setResultSuccess();
    }

    //查詢條件查詢
    @Override
    public Result<PageInfo<BrandEntity>> getBrandInfo(BrandDTO brandDTO) {
        //  List<BrandEntity> list = brandMapper.selectAll();
        //PageHelper.startPage(brandDTO.getPage(), brandDTO.getRows());
        if(ObjectUtil.isNotNull(brandDTO.getPage()) && ObjectUtil.isNotNull(brandDTO.getRows()))
            PageHelper.startPage(brandDTO.getPage(),brandDTO.getRows());
        ////
        //排序簡化版
        if(!StringUtil.isEmpty(brandDTO.getSort()))  PageHelper.orderBy(brandDTO.getOrderByClause());

//        if(!StringUtil.isEmpty(brandDTO.getSort())){
//            String order = "";
//            if(Boolean.valueOf(brandDTO.getOrder())){
//                order = "desc";
//            }else{
//                order = "asc";
//            }
//            PageHelper.orderBy(brandDTO.getSort()+ " " + (StringUtil.isEmpty(brandDTO.getSort())?"desc" : "asc"));
//        }


        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO,BrandEntity.class);


        Example example = new Example(BrandEntity.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(brandEntity.getName()))
        //example.createCriteria().andLike("name","%"+brandEntity.getName()+"%");
        criteria.andLike("name","%" + brandEntity.getName() + "%");
        if(ObjectUtil.isNotNull(brandDTO.getId()))
            criteria.andEqualTo("id",brandDTO.getId());
        List<BrandEntity> brandEntities = brandMapper.selectByExample(example);

        PageInfo<BrandEntity> list = new PageInfo<>(brandEntities);

        return this.setResultSuccess(list);

    }

    @Override
    public Result<List<BrandEntity>> getBrandInfoByCategoryId(Integer cid) {
        List<BrandEntity> list = brandMapper.getBrandInfoByCategoryId(cid);
        return this.setResultSuccess(list);
    }


    private void deleteCategoryBrandByBrandId(Integer brandId){

        Example example = new Example(CategoryBrandEntity.class);
        example.createCriteria().andEqualTo("brandId",brandId);
        categoryBrandMapper.deleteByExample(example);

    }



    private void insertCategoryBrandList(String categories, Integer brandId){

        //将公共的代码抽取出来
        //看是否需要返回值
        //看抽取出来的方法是否需要别的类调用
        //抽取出来的代码哪里报错,查看报错信息,用方法的参数代替(可变的内容当做方法的参数)
        //如果有不重要的返回值代码 --> 手动抛自定义异常(全局异常处理会帮我们处理)

        // 自定义异常
        if(StringUtils.isEmpty(categories)) throw new RuntimeException("分类信息不能为空");

        //判断分类集合字符串中是否包含,
        if(categories.contains(",")){//多个分类 --> 批量新增

            categoryBrandMapper.insertList(
                    Arrays.asList(categories.split(","))
                            .stream()
                            .map(categoryIdStr -> new CategoryBrandEntity(Integer.valueOf(categoryIdStr)
                                    ,brandId))
                            .collect(Collectors.toList())
            );

        }else{//普通单个新增

            CategoryBrandEntity categoryBrandEntity = new CategoryBrandEntity();
            categoryBrandEntity.setBrandId(brandId);
            categoryBrandEntity.setCategoryId(Integer.valueOf(categories));

            categoryBrandMapper.insertSelective(categoryBrandEntity);
        }
    }


}



