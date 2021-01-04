package com.baidu.shop.servce.impl;

import com.baidu.shop.mapper.CategoryMapper;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.service.CategoryService;
import com.baidu.shop.utils.ObjectUtil;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
/**
 * 2 *@ClassName CategoryServiceImpl
 * 3 *@Description: TODO
 * 4 *@Author 王振方
 * 5 *@Date 2020/12/22
 *
 * @Version V1.0
 * 7
 **/
@RestController
public class CategoryServiceImpl extends BaseApiService implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Transactional
    @Override
    public Result<JsonObject> addCategory(@RequestBody CategoryEntity entity) {

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setIsParent(1);
        categoryEntity.setId(entity.getParentId());
        categoryMapper.updateByPrimaryKeySelective(categoryEntity);

        categoryMapper.insertSelective(entity);
        return this.setResultSuccess();
    }

    @Override
    public Result<List<CategoryEntity>> getCategoryByPid(Integer pid) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setParentId(pid);
        List<CategoryEntity> list = categoryMapper.select(categoryEntity);
        return this.setResultSuccess(list);
    }

    @Override
    public Result<List<CategoryEntity>> getCategoryBrandByPid(Integer brandId) {
        List<CategoryEntity> byBrandId = categoryMapper.getByBrandId(brandId);
        return this.setResultSuccess(byBrandId);
    }

    @Transactional
    @Override
    public Result<JsonObject> editCategory(@RequestBody CategoryEntity entity) {
        categoryMapper.updateByPrimaryKeySelective(entity);
        return this.setResultSuccess();
    }

    @Transactional
    @Override
    public Result<JsonObject> delCategory(Integer id) {
        //驗證傳入的id是否有效，并且查詢出來的數據對接下來的程序有用
        CategoryEntity categoryEntity = categoryMapper.selectByPrimaryKey(id);
        if(ObjectUtil.isNull(categoryEntity)){
            return this.setResultError("當前id不存在");
        }
        //判斷當前節點是否為父節點
        if(categoryEntity.getIsParent() ==1){
            return this.setResultError("當前節點為父節點，不能刪除");
        }

//        if(){
//
//        }
//        Example example = new Example(CategoryEntity.class);
//        Example.Criteria criteria = example.createCriteria();
//        criteria.andEqualTo("sparceId",categoryEntity.getParentId());
//        List<CategoryEntity> list = categoryMapper.selectByExample(example);


        //構建條件查詢 ，通過當前被刪除節點的parentid查詢數據
        //查詢當前節點的父id， 下是否有其他的子節點
        Example example = new Example(CategoryEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId",categoryEntity.getParentId());
        List<CategoryEntity> list = categoryMapper.selectByExample(example);



        //如果查詢出來的數據只有一條  將父節點isParent的狀態改爲0
        if(list.size() <= 1){
            CategoryEntity isParentEntity = new CategoryEntity();
            isParentEntity.setId(categoryEntity.getParentId());
            isParentEntity.setIsParent(0);
            categoryMapper.updateByPrimaryKeySelective(isParentEntity);
        }

        categoryMapper.deleteByPrimaryKey(id); //執行刪除
        return this.setResultSuccess();
    }


}
