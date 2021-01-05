package com.baidu.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;

/**
 * 2 *@ClassName CategoryBrandEntity
 * 3 *@Description: TODO
 * 4 *@Author 王振方
 * 5 *@Date 2020/12/28
 *
 * @Version V1.0
 * 7
 **/

@Data
@Table(name = "tb_category_brand")
@NoArgsConstructor
@AllArgsConstructor
public class CategoryBrandEntity {

    private Integer categoryId;

    private Integer brandId;

}



