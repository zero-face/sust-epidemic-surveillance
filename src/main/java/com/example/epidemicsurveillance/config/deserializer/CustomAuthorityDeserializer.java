package com.example.epidemicsurveillance.config.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 自定义Authority解析器（反序列化）
 * 更新用户信息时,"authority"无法解析
 * @ClassName CustomAuthorityDeserializer
 * @Author 朱云飞
 * @Date 2021/6/26 18:09
 * @Version 1.0
 **/

public class CustomAuthorityDeserializer extends JsonDeserializer {
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
       //拿取JSON里面的信息
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode jsonNode = mapper.readTree(p);
        List<GrantedAuthority> grantedAuthorityList=new LinkedList<>();
        Iterator<JsonNode> elements = jsonNode.elements();
        //遍历获取authority
        while (elements.hasNext()){
            JsonNode next = elements.next();
            JsonNode authority = next.get("authority");
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority.asText());
            grantedAuthorityList.add(simpleGrantedAuthority);
        }
        //返回角色，通过Admin中重写的方法设置的Role中
        return grantedAuthorityList;
    }
}
