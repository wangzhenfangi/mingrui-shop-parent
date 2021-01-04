package com.baidu.shop.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

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
    private Integer id;

    private Integer cid;

    private Integer groupId;

    private String name;

    //这句sql不会执行错误
    //加上``会当成普通字符串处理
    @Column(name = "`numeric`")
    private Boolean numeric;

    private String unit;

    private Boolean generic;

    private Boolean searching;

    private String segments;
}
