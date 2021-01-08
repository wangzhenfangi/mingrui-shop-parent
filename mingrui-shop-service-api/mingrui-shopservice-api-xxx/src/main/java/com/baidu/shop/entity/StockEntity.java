package com.baidu.shop.entity;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 2 *@ClassName StockEntity
 * 3 *@Description: TODO
 * 4 *@Author 王振方
 * 5 *@Date 2021/1/7
 *
 * @Version V1.0
 * 7
 **/
@Data
@Table(name = "tb_stock")
public class StockEntity {

    @Id
    @ApiModelProperty(value = "skuId主鍵",example = "1")
    @NotNull(message = "skuId主键不能为空",groups = {MingruiOperation.Update.class})
    private Long skuId;

    private Integer seckillStock;

    private Integer seckillTotal;

    @ApiModelProperty(value = "库存不能为空",example = "1")
    @NotNull(message = "库存不能为空",groups = {MingruiOperation.Update.class,MingruiOperation.Add.class})
    private Integer stock;

}
