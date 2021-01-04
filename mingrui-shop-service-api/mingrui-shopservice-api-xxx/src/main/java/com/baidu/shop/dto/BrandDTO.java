package com.baidu.shop.dto;

import com.baidu.shop.base.BaseDTO;
import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 2 *@ClassName BrandDTO
 * 3 *@Description: TODO
 * 4 *@Author 王振方
 * 5 *@Date 2020/12/25
 *
 * @Version V1.0
 * 7
 **/

@Data
@ApiModel(value ="品牌DTO")
public class BrandDTO extends BaseDTO {

    @ApiModelProperty(value = "品牌主鍵")
    @NotNull(message = "主鍵不能爲空",groups = {MingruiOperation.Update.class})
    private Integer id;

    @ApiModelProperty(value="品牌名稱")
    @NotEmpty(message = "品牌名稱不能爲空",groups = {MingruiOperation.Update.class,MingruiOperation.Add.class})
    private String  name;
    @ApiModelProperty(value ="品牌圖片")
    private String image;
    @ApiModelProperty(value = "品牌首字母")
    private  Character letter;
    ///////////////////////////////////////////////
    @ApiModelProperty(value = "品牌分类信息")
    @NotEmpty(message = "品牌分类信息不能为空",groups = {MingruiOperation.Add.class})
    private String categories;



}
