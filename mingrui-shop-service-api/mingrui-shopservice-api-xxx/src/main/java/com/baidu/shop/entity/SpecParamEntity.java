package com.baidu.shop.entity;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 2 *@ClassName SpecParamEntity
 * 3 *@Description: TODO
 * 4 *@Author 王振方
 * 5 *@Date 2021/1/4
 *
 * @Version V1.0
 * 7
 **/

@Table(name = "tb_spec_param")
@Data
public class SpecParamEntity {
    @Id
    @ApiModelProperty(value = "SpecParam主鍵",example = "1")
    @NotNull(message = "主键不能为空",groups = {MingruiOperation.Update.class})
    private Integer id;

    @ApiModelProperty(value = "cid",example = "1")
    @NotNull(message = "cid不能为空",groups = {MingruiOperation.Update.class,MingruiOperation.Add.class})
    private Integer cid;

    @ApiModelProperty(value = "groupId",example = "1")
    @NotNull(message = "groupId不能为空",groups = {MingruiOperation.Update.class,MingruiOperation.Add.class})
    private Integer groupId;

    @ApiModelProperty(value = "规格参数名")
    @NotEmpty(message = "规格参数名不能为空",groups = {MingruiOperation.Update.class,MingruiOperation.Add.class})
    private String name;

    //这句sql不会执行错误
    //加上``会当成普通字符串处理
    @Column(name = "`numeric`")
    private Boolean numeric;

    private String unit;

//    @ApiModelProperty(value = "通用规格参数",example = "1")
//    @NotNull(message = "通用规格参数不能为空",groups = {MingruiOperation.Update.class,MingruiOperation.Add.class})
    private Boolean generic;

    private Boolean searching;

    private String segments;
}
