package com.example.epidemicsurveillance.utils.gov;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zero
 * @date 2021/10/22 14:26
 * @description
 * @since 1.8
 **/
@Component
public class GuideUtil {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${gov.appId}")
    private String appId;
    @Value("${gov.key}")
    private String key;
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

    public void GuideData(String city) throws URISyntaxException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        HttpHeaders headers = headers(timestamp);
        Map<String,Object> maps = new HashMap<>();
        maps.put("appId", appId);
        maps.put("key",key);
        maps.put("nonceHeader", nonceHeader);
        maps.put("paasHeader",paasHeader);
        maps.put("timestampHeader", timestamp);
        maps.put("code", getCityCode(city));
        maps.put("signatureHeader", signature(timestamp + playSecret + nonceHeader + timestamp));
        final HttpEntity<Map<String,Object>> httpEntity = new HttpEntity<>(maps, headers);
        final ResponseEntity<String> responseEntity = restTemplate.exchange(new URI("http://103.66.32.242:8005/zwfwMovePortal/interface/interfaceJson"),
                HttpMethod.POST, httpEntity, String.class);
        if(responseEntity.getStatusCodeValue()==200) {
            System.out.println("获取成功");
            System.out.println(responseEntity.getBody());
        }


    }

    /**
     * 获取城市的编码
     * @return
     */
    private Object getCityCode(String city) {
        return null;
    }

    /**
     * 构建请求头
     * @param timestamp
     * @return
     */
    private HttpHeaders headers(String timestamp) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-wif-nonce", x_wif_nonce);
        headers.add("x-wif-nonce", x_wif_paasid);
        headers.add("x-wif-timestamp", timestamp);
        headers.add("x-wif-signature", signature(timestamp + headSecret + timestamp));
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
        return encodeStr;
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
