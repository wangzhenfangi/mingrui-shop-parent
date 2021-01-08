package com.baidu.shop.entity;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 2 *@ClassName SpuDetailEntity
 * 3 *@Description: TODO
 * 4 *@Author 王振方
 * 5 *@Date 2021/1/7
 *
 * @Version V1.0
 * 7
 **/
@Data
@Table(name = "tb_spu_detail")
public class SpuDetailEntity{

    @Id
    @ApiModelProperty(value = "spuId主鍵",example = "1")
    @NotNull(message = "spuId不能为空",groups = {MingruiOperation.Update.class})
    private Integer spuId;

    private String description;

    @ApiModelProperty(value = "通用参数")
    @NotEmpty(message = "通用参数不能为空",groups = {MingruiOperation.Update.class,MingruiOperation.Add.class})
    private String genericSpec;

    @ApiModelProperty(value = "特殊参数")
    @NotEmpty(message = "特殊参数不能为空",groups = {MingruiOperation.Update.class,MingruiOperation.Add.class})
    private String specialSpec;

    private String packingList;

    private String afterService;



}
