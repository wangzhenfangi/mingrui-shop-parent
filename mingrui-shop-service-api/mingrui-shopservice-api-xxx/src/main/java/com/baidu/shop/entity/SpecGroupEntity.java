package com.baidu.shop.entity;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 2 *@ClassName SpecGroupEntity
 * 3 *@Description: TODO
 * 4 *@Author 王振方
 * 5 *@Date 2021/1/4
 *
 * @Version V1.0
 * 7
 **/
@Table(name = "tb_spec_group")
@Data
public class SpecGroupEntity {
    @Id
    @ApiModelProperty(value = "SpecGroup主鍵",example = "1")
    @NotNull(message = "主键不能为空",groups = {MingruiOperation.Update.class})
    private  Integer id;

    @ApiModelProperty(value = "cid",example = "1")
    @NotNull(message = "cid不能为空",groups = {MingruiOperation.Update.class,MingruiOperation.Add.class})
    private  Integer cid;

    @ApiModelProperty(value = "SpecGroup名")
    @NotEmpty(message = "SpecGroup名不能为空",groups = {MingruiOperation.Update.class,MingruiOperation.Add.class})
    private  String name;


}
