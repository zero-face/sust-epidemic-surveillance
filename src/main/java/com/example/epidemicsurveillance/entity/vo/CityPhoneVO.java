package com.example.epidemicsurveillance.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Zero
 * @date 2021/10/23 21:42
 * @description
 * @since 1.8
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CityPhoneVO {
    private String code;

    private String county;

    private String province;

    private String city;

    private String telephone;

    private String serviceTime;
}
