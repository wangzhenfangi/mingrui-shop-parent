package com.baidu.shop.entity;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 2 *@ClassName CategoryEntity
 * 3 *@Description: TODO
 * 4 *@Author 王振方
 * 5 *@Date 2020/12/22
 *
 * @Version V1.0
 * 7
 **/
@ApiModel(value="分類實體類")
@Data
@Table(name="tb_category")
public class CategoryEntity {

    @Id
    @ApiModelProperty(value = "分類主鍵",example = "1")
    @NotNull(message = "id 不能爲空",groups ={MingruiOperation.Update.class})
    private Integer Id;  //如果没有@Column 默认使用entity的属性名和 数据库表的字段名做映射

    @ApiModelProperty(value = "分类名称")
    @NotEmpty(message = "分類名稱不能爲空",groups = {MingruiOperation.Add.class,MingruiOperation.Add.class})
    private String name;

    @ApiModelProperty(value = "父级分类",example = "1")

    @NotNull(message = "父級分類不能爲空",groups = {MingruiOperation.Add.class})
    private Integer parentId;

    @ApiModelProperty(value = "是否是父级节点",example = "1")
    @NotNull(message = "是否爲父節點不能爲空",groups = {MingruiOperation.Add.class})
    private Integer isParent;

    @ApiModelProperty(value = "排序",example = "1")
    @NotNull(message = "排序字段不能爲空",groups = {MingruiOperation.Add.class})
    private Integer sort;

}
