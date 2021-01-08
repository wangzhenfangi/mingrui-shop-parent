package com.baidu.shop.entity;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 2 *@ClassName SpuEntity
 * 3 *@Description: TODO
 * 4 *@Author 王振方
 * 5 *@Date 2021/1/5
 *
 * @Version V1.0
 * 7
 **/
@Data
@Table(name = "tb_spu")
public class SpuEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "spu主鍵",example = "1")
    @NotNull(message = "主键不能为空",groups = {MingruiOperation.Update.class})
    private Integer id;

    @ApiModelProperty(value = "标题")
    @NotEmpty(message = "标题不能为空",groups = {MingruiOperation.Update.class,MingruiOperation.Add.class})
    private String title;

    private String subTitle;

    @ApiModelProperty(value = "cid1",example = "1")
    @NotNull(message = "cid1不能为空",groups = {MingruiOperation.Update.class,MingruiOperation.Add.class})
    private Integer cid1;

    @ApiModelProperty(value = "cid2",example = "1")
    @NotNull(message = "cid2不能为空",groups = {MingruiOperation.Update.class,MingruiOperation.Add.class})
    private Integer cid2;

    @ApiModelProperty(value = "cid3",example = "1")
    @NotNull(message = "cid3不能为空",groups = {MingruiOperation.Update.class,MingruiOperation.Add.class})
    private Integer cid3;

    @ApiModelProperty(value = "品牌id",example = "1")
    @NotNull(message = "品牌id不能为空",groups = {MingruiOperation.Update.class,MingruiOperation.Add.class})
    private Integer brandId;

    @ApiModelProperty(value = "是否上架",example = "1")
    @NotNull(message = "上架下架不能为空",groups = {MingruiOperation.Update.class,MingruiOperation.Add.class})
    private Integer saleable;

    @ApiModelProperty(value = "有效",example = "1")
    @NotNull(message = "有效信息不能为空",groups = {MingruiOperation.Update.class,MingruiOperation.Add.class})
    private Integer valid;

    private Date createTime;

    private Date lastUpdateTime;



}
