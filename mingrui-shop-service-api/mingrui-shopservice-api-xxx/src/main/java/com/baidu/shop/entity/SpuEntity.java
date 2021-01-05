package com.baidu.shop.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
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
@Table(name = "tb_sup")
public class SpuEntity{

    @Id
    private Integer id;

    private String title;

    private String subTitle;

    private Integer cid1;

    private Integer cid2;

    private Integer cid3;

    private Integer brandId;

    private Integer saleable;

    private Integer valid;

    private Date createTime;

    private Date lastUpdateTime;



}