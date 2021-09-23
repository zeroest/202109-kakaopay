package me.zeroest.kyd_kakaopay.config.swagger;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
@ApiModel
public class SwaggerPage {

    @ApiModelProperty(value = "페이지 번호(0..N)")
    private Integer page;

    @ApiModelProperty(value = "페이지 크기") //, allowableValues="range[0, 100]")
    private Integer size;

}
