package com.baidu.shop.response;

import com.baidu.shop.base.Result;
import com.baidu.shop.document.GoodsDoc;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.status.HTTPStatus;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 2 *@ClassName GoodsResponse
 * 3 *@Description: TODO
 * 4 *@Author 王振方
 * 5 *@Date 2021/3/6
 *
 * @Version V1.0
 * 7
 **/
@Data
@NoArgsConstructor
public class GoodsResponse extends Result<List<GoodsDoc>> {

    private Long total;

    private Long totalPage;

    private List<CategoryEntity> categoryList;

    private List<BrandEntity> brandList;

    public GoodsResponse(Long total, Long totalPage, List<CategoryEntity> categoryList
            , List<BrandEntity> brandList,List<GoodsDoc> goodsDocList){
        //父类的构造函数
        super(HTTPStatus.OK,"",goodsDocList);

        this.total = total;
        this.totalPage = totalPage;
        this.categoryList = categoryList;
        this.brandList = brandList;
    }
}
