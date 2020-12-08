package com.example.demo.common.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fg
 * @date 2020/11/5
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "统一返回对象")
public class ResultVO<T> {

    @ApiModelProperty(value = "返回码")
    private Integer code;

    @ApiModelProperty(value = "返回信息")
    private String msg;

    @ApiModelProperty(value = "时间戳")
    private Long timestamp;

    @ApiModelProperty(value = "返回数据")
    private T data;

}
