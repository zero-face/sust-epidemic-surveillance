package com.example.epidemicsurveillance.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.epidemicsurveillance.entity.CityCode;
import com.example.epidemicsurveillance.exception.EpidemicException;
import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.ICityCodeService;
import com.example.epidemicsurveillance.utils.nat_facility.NATUtil;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author Zero
 * @date 2021/10/30 17:47
 * @description
 * @since 1.8
 **/
@RestController
@Slf4j
@RequestMapping("/api/v1/user/nat")
@Validated
public class NATController {

    @Autowired
    private NATUtil natUtil;

    @Autowired
    private ICityCodeService cityCodeService;

    @GetMapping
    public ResponseResult NATQuery(@RequestParam(value = "province",required = false) @NotBlank String province,
                                   @RequestParam("search_key") @NotBlank String search_key,
                                   @RequestParam("pn") @Min(value = 1,message = "页数最小只能是1") Integer  pn,
                                   @RequestParam("pageSize") @NotBlank String pageSize) throws JSONException {
        final CityCode city = cityCodeService.getOne(new QueryWrapper<CityCode>().eq(province != null, "city", province));
        if(null == city) {
            throw new EpidemicException("请选择正确的省份");
        }
        final String body = natUtil.PlacesFind(city.getCode(), search_key, pn, pageSize);
        final JSONObject jsonObject = JSON.parseObject(body);
        final Object data = jsonObject.get("data");
        Map<String, Object> queryData = JSON.parseObject(JSON.toJSONString(data), new TypeReference<Map<String, Object>>() {});
        return ResponseResult.ok().message("获取成功").data(queryData);
    }

}
