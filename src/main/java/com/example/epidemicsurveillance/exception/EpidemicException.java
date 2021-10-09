package com.example.epidemicsurveillance.exception;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义异常类
 *
 * @ClassName EducationException
 * @Author 朱云飞
 * @Date 2021/8/30 10:58
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpidemicException extends RuntimeException{
    @ApiModelProperty(value = "状态码")
    private Integer code;

    @ApiModelProperty(value = "异常信息")
    private String message;

    public EpidemicException(String message){
        this.code=20001;
        this.message=message;
    }

    @Override
    public String toString() {
        return "EducationException{" +
                "message=" + this.getMessage() +
                ", code=" + code +
                '}';
    }
}
