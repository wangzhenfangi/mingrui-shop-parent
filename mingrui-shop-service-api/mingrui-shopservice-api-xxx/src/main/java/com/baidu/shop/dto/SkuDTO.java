package com.baidu.shop.dto;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jdk.nashorn.internal.ir.CallNode;
import lombok.Data;
import sun.dc.pr.PRError;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 2 *@ClassName skuDTO
 * 3 *@Description: TODO
 * 4 *@Author 王振方
 * 5 *@Date 2021/1/7
 *
 * @Version V1.0
 * 7
 **/
@Data
@ApiModel(value = "sku属性数据传输类")
public class SkuDTO {

    @ApiModelProperty(value = "主键", example = "1")
    @NotNull(message = "id不能为空",groups = {MingruiOperation.Update.class})
    private Long id;

    @ApiModelProperty(value = "spu主键", example = "1")
    @NotNull(message = "spuId不能为空",groups = {MingruiOperation.Update.class})
    private Integer spuId;

    @ApiModelProperty(value = "商品标题")
    @NotEmpty(message = "标题不能为空",groups = {MingruiOperation.Update.class,MingruiOperation.Add.class})
    private String title;

    @ApiModelProperty(value = "商品的图片，多个图片以‘,’分割")
    private String images;

    @ApiModelProperty(value = "销售价格，单位为分", example = "1")
    @NotNull(message = " 价格不能为空",groups = {MingruiOperation.Update.class,MingruiOperation.Add.class})
    private Integer price;

    @ApiModelProperty(value = "特有规格属性在spu属性模板中的对应下标组合")
    private String indexes;

    @ApiModelProperty(value = "sku的特有规格参数键值对，json格式，反序列化时请使用 linkedHashMap，保证有序")
    private String ownSpec;

    //注意此处使用boolean值来接,在service中处理一下就可以了
    @ApiModelProperty(value = "是否有效，0无效，1有效", example = "1")
    // @NotNull(message = "enable不能为空",groups = {MingruiOperation.Update.class,MingruiOperation.Add.class})
    private Boolean enable;

    @ApiModelProperty(value = "添加时间")
    private Date createTime;

    @ApiModelProperty(value = "最后修改时间")
    private Date lastUpdateTime;

    @ApiModelProperty(value = "库存")
    private Integer stock;


}
