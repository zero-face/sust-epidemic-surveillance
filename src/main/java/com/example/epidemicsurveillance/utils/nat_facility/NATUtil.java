package com.example.epidemicsurveillance.utils.nat_facility;

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
import java.util.Locale;
import java.util.Map;

/**
 * @author Zero
 * @date 2021/10/30 15:56
 * @description
 * @since 1.8
 **/
@Component
@Slf4j
public class NATUtil {

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

    @Value("${gov.placeKey}")
    private String key;

    @Value("${gov.placetoken}")
    private String token;
    @Value("${gov.placeHeaderKey}")
    private String placeHeaderKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CityCodeMapper cityCodeMapper;

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36";

    @Autowired
    private EmailSendUtil emailSendUtil;

    public String PlacesFind(String cityCode,String search_key,Integer pn,String pageSize) {
        log.info("????????????{}????????????????????????????????????:{}",cityCode,search_key);
        String timestamp = String.valueOf(System.currentTimeMillis()/1000);
        //???????????????
        HttpHeaders headers = headers(timestamp);

        Map<String,Object> maps = new HashMap<>();
        maps.put("appId", appId);
        maps.put("key",key);
        maps.put("nonceHeader", nonceHeader);
        maps.put("paasHeader",paasHeader);
        maps.put("timestampHeader", timestamp);
        maps.put("code", cityCode);
        maps.put("page", pn);
        maps.put("page_size", pageSize);
        maps.put("serach_key", search_key);
        maps.put("signatureHeader", signature(timestamp + token + nonceHeader + timestamp));
        final HttpEntity httpEntity = new HttpEntity(maps, headers);
        final ResponseEntity<String> exchange = restTemplate.exchange("http://103.66.32.242:8005/zwfwMovePortal/interface/interfaceJson",
                HttpMethod.POST, httpEntity, String.class);
        if(exchange.getStatusCodeValue()==200) {
            log.info("??????????????????????????????:{}",exchange.getBody());
            return exchange.getBody();
        } else {
            log.error("????????????????????????????????????");
            emailSendUtil.sendEmailToAdmin("1444171773@qq.com", "??????{"+ cityCode + ":" + search_key +"}?????????????????????");
            throw new EpidemicException("????????????????????????:" + cityCode + ":" + search_key);
        }
    }

    /**
     * ???????????????
     * @param timestamp
     * @return
     */
    public HttpHeaders headers(String timestamp) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("User-Agent", USER_AGENT);
        headers.set("x-wif-nonce", x_wif_nonce);
        headers.set("x-wif-paasid", x_wif_paasid);
        headers.set("x-wif-timestamp", timestamp);
        headers.set("x-wif-signature", signature(timestamp + placeHeaderKey + x_wif_nonce + timestamp));
        return headers;
    }
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
        String sign = encodeStr.toUpperCase();
        log.info("?????????????????????{}",encodeStr.toUpperCase());
        return encodeStr.toUpperCase();
    }
    private String byte2Hex(byte[] digest) {
        StringBuilder stringBuilder = new StringBuilder();
        String temp;
        for (byte aByte : digest) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                //1????????????????????????0??????
                stringBuilder.append("0");
            }
            stringBuilder.append(temp);
        }
        return stringBuilder.toString();
    }
}
