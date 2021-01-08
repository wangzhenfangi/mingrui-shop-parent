package com.baidu.shop.entity;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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

    @ApiModelProperty(value = "categoryId主鍵",example = "1")
    @NotNull(message = "categoryId主键不能为空",groups = {MingruiOperation.Update.class})
    private Integer categoryId;

    @ApiModelProperty(value = "brandId主鍵",example = "1")
    @NotNull(message = "brandId主键不能为空",groups = {MingruiOperation.Update.class})
    private Integer brandId;

}



