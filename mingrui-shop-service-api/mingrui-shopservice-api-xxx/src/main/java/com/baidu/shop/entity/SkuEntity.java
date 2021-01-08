package com.baidu.shop.entity;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 2 *@ClassName SkuEntity
 * 3 *@Description: TODO
 * 4 *@Author 王振方
 * 5 *@Date 2021/1/7
 *
 * @Version V1.0
 * 7
 **/

@Data
@Table(name = "tb_sku")
public class SkuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "sku主鍵",example = "1")
    @NotNull(message = "id不能为空",groups = {MingruiOperation.Update.class})
    private Long id;

    @ApiModelProperty(value = "spuId不能为空",example = "1")
    @NotNull(message = "spuId不能为空",groups = {MingruiOperation.Update.class})
    private Integer spuId;

    @ApiModelProperty(value = "标题")
    @NotEmpty(message = "标题不能为空",groups = {MingruiOperation.Update.class,MingruiOperation.Add.class})
    private String title;

    @ApiModelProperty(value = "图片")
    private String images;

    @ApiModelProperty(value = " 价格不能为空",example = "1")
    @NotNull(message = " 价格不能为空",groups = {MingruiOperation.Update.class,MingruiOperation.Add.class})
    private Integer price;

    private String indexes;

    private String ownSpec;

    @ApiModelProperty(value = "enable不能为空",example = "1")
    @NotNull(message = "enable不能为空",groups = {MingruiOperation.Update.class,MingruiOperation.Add.class})
    private Integer enable;

    private Date createTime;

    private Date lastUpdateTime;


}
