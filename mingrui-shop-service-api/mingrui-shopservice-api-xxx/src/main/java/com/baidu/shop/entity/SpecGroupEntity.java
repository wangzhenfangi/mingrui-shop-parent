package com.baidu.shop.entity;

import lombok.Data;

import javax.persistence.Table;

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

    private  Integer id;

    private  Integer cid;

    private  String name;

}
