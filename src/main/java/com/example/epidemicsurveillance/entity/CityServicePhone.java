package com.example.epidemicsurveillance.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Zero
 * @date 2021/10/23 21:29
 * @description
 * @since 1.8
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CityServicePhone {
    private String id;

    private String code;

    private String city;

    private String province;

    private String serverTime;

    private String county;

    private String phone;
}
