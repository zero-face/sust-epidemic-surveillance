package com.example.epidemicsurveillance.utils.citypolicy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.epidemicsurveillance.entity.CityCode;
import com.example.epidemicsurveillance.exception.EpidemicException;
import com.example.epidemicsurveillance.mapper.CityCodeMapper;
import com.example.epidemicsurveillance.utils.rabbitmq.EmailSendUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zero
 * @date 2021/10/23 13:18
 * @description
 * @since 1.8
 **/
@Slf4j
@Component
public class PolicyUtil {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${gov.appId}")
    private String appId;

    @Value("${gov.nonceHeader}")
    private String nonceHeader;

    @Value("${gov.paasHeader}")
    private String paasHeader;

    @Value("${gov.headSecret}")
    private String headSecret;

    @Value("${gov.playSecret}")
    private String playSecret;

    @Value("${gov.x-wif-nonce}")
    private String x_wif_nonce;

    @Value("${gov.x-wif-paasid}")
    private String x_wif_paasid;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CityCodeMapper cityCodeMapper;

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36";

    @Autowired
    private EmailSendUtil emailSendUtil;

    public String GuideData(String city,String key) {
        log.info("开始获取防控{}的政策",city);
        String timestamp = String.valueOf(System.currentTimeMillis()/1000);
        HttpHeaders headers = headers(timestamp);
        final CityCode cityCode = getCityCode(city);
        log.info("组装请求头完成");
        Map<String,String> maps = new HashMap<>();
        maps.put("appId", appId);
        maps.put("key",key);
        maps.put("nonceHeader", nonceHeader);
        maps.put("paasHeader",paasHeader);
        maps.put("timestampHeader", timestamp);
        maps.put("code", cityCode.getCode());
        maps.put("signatureHeader", signature(timestamp + playSecret + nonceHeader + timestamp));
        final HttpEntity httpEntity = new HttpEntity(maps, headers);
        final ResponseEntity<String> exchange = restTemplate.exchange("http://103.66.32.242:8005/zwfwMovePortal/interface/interfaceJson",
                HttpMethod.POST, httpEntity, String.class);
        if(exchange.getStatusCodeValue()==200) {
            log.info("获取成功:{}",exchange.getBody());
           return exchange.getBody();
        } else {
            log.error("获取疫情防控信息失败");
            emailSendUtil.sendEmailToAdmin("1444171773@qq.com", "获取{"+city +"}的防控政策数据失败");
            throw new EpidemicException("获取防控信息失败:" + city);
        }
    }

    /**
     * 获取城市的编码
     * @return
     */
    private CityCode getCityCode(String city) {
        final CityCode cityCode = cityCodeMapper.selectOne(new QueryWrapper<CityCode>().eq(true, "city", city));
        if(cityCode == null) {
            throw new EpidemicException("请输入正确的城市");
        }
        return cityCode;
    }

    /**
     * 构建请求头
     * @param timestamp
     * @return
     */
    private HttpHeaders headers(String timestamp) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("User-Agent", USER_AGENT);
        headers.set("x-wif-nonce", x_wif_nonce);
        headers.set("x-wif-paasid", x_wif_paasid);
        headers.set("x-wif-timestamp", timestamp);
        headers.set("x-wif-signature", signature(timestamp + headSecret + timestamp));
        return headers;
    }

    /**
     * 生成请求头signature
     * @return
     */
    private String signature(String text) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(text.getBytes(StandardCharsets.UTF_8));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        log.info("生成的签名为：{}",encodeStr.toUpperCase());
        return encodeStr.toUpperCase();
    }
    private String byte2Hex(byte[] digest) {
        StringBuilder stringBuilder = new StringBuilder();
        String temp;
        for (byte aByte : digest) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuilder.append("0");
            }
            stringBuilder.append(temp);
        }
        return stringBuilder.toString();
    }
}
