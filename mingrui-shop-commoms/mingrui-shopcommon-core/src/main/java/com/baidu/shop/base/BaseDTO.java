package com.baidu.shop.base;

import com.netflix.discovery.util.StringUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * 2 *@ClassName BaseDTO
 * 3 *@Description: TODO
 * 4 *@Author 王振方
 * 5 *@Date 2020/12/25
 *
 * @Version V1.0
 * 7
 **/

@Data
@ApiModel(value = "baseDTO用于數據傳輸,其他DTO需要繼承此類")
public class BaseDTO {

    @ApiModelProperty(value = "當前頁",example = "1")
    private Integer page;

    @ApiModelProperty(value = "每頁顯示幾條數據",example = "5")
    private Integer rows;

    @ApiModelProperty(value = "排序字段")
    private String sort;

    @ApiModelProperty(value="是否升序")
    private String order;

    @ApiModelProperty(hidden = true)
    public String getOrderByClause(){
                                            //desc 降序排序  asc  升序排序
        return sort + " " +(Boolean.valueOf(order)?"desc" : "asc");
    }


}
