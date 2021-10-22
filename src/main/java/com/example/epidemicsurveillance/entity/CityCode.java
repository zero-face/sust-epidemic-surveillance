package com.example.epidemicsurveillance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Zero
 * @date 2021/10/22 15:17
 * @description
 * @since 1.8
 **/
@Data
@TableName("code_city")
public class CityCode {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String code;

    private String city;
}
