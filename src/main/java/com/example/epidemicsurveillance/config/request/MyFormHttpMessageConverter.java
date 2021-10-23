package com.example.epidemicsurveillance.config.request;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Zero
 * @Date 2021/8/25 22:43
 * @Since 1.8
 * @Description 用于为RestTemplate提供将Map转换为UrlEncoded的能力
 **/
@Configuration
public class MyFormHttpMessageConverter implements HttpMessageConverter<Map<String, ?>> {
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final Charset charset = DEFAULT_CHARSET;
    private static final MediaType DEFAULT_FORM_DATA_MEDIA_TYPE =
            new MediaType(MediaType.APPLICATION_JSON, DEFAULT_CHARSET);

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        if (!Map.class.isAssignableFrom(clazz)) {
            return false;
        }
        if (mediaType == null) {
            return true;
        }
        for (MediaType supportedMediaType : getSupportedMediaTypes()) {
            if (supportedMediaType.includes(mediaType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        if (!Map.class.isAssignableFrom(clazz)) {
            return false;
        }
        if (mediaType == null || MediaType.ALL.equals(mediaType)) {
            return true;
        }
        for (MediaType supportedMediaType : getSupportedMediaTypes()) {
            if (supportedMediaType.isCompatibleWith(mediaType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Collections.singletonList(MediaType.APPLICATION_JSON);
    }

    @Override
    public Map<String, ?> read(Class<? extends Map<String, ?>> clazz, HttpInputMessage inputMessage) throws IOException,
            HttpMessageNotReadableException {
        MediaType contentType = inputMessage.getHeaders().getContentType();
        Charset charset = (contentType != null && contentType.getCharset() != null ?
                contentType.getCharset() : this.charset);

        String body = StreamUtils.copyToString(inputMessage.getBody(), charset);
        //根据设定的标识符来分割字符串为字符数组
        String[] pairs = StringUtils.tokenizeToStringArray(body, "&");
        Map<String, String> result = new HashMap<>(pairs.length);
        for (String pair : pairs) {
            int idx = pair.indexOf('=');
            if (idx == -1) {
                //如果没有带参数
                result.put(URLDecoder.decode(pair, charset.name()), null);
            }
            else {
                //带有参数就分别解码
                String name = URLDecoder.decode(pair.substring(0, idx), charset.name());
                String value = URLDecoder.decode(pair.substring(idx + 1), charset.name());
                result.put(name, value);
            }
        }

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void write(Map<String, ?> map, MediaType contentType, HttpOutputMessage outputMessage) throws IOException,
            HttpMessageNotWritableException {
        writeForm((Map<String, Object>) map, contentType, outputMessage);
    }

    public void writeForm(Map<String, Object> formData, @Nullable MediaType contentType,
                          HttpOutputMessage outputMessage) throws IOException {
        contentType = getFormContentType(contentType);
        outputMessage.getHeaders().setContentType(contentType);

        Charset charset = contentType.getCharset();
        // should never occur
        Assert.notNull(charset, "No charset");

        byte[] bytes = serializeForm(formData, charset).getBytes(charset);
        outputMessage.getHeaders().setContentLength(bytes.length);

        if (outputMessage instanceof StreamingHttpOutputMessage) {
            StreamingHttpOutputMessage streamingOutputMessage = (StreamingHttpOutputMessage) outputMessage;
            streamingOutputMessage.setBody(outputStream -> StreamUtils.copy(bytes, outputStream));
        }
        else {
            StreamUtils.copy(bytes, outputMessage.getBody());
        }
    }

    protected MediaType getFormContentType(@Nullable MediaType contentType) {
        if (contentType == null) {
            return DEFAULT_FORM_DATA_MEDIA_TYPE;
        }
        else if (contentType.getCharset() == null) {
            return new MediaType(contentType, this.charset);
        }
        else {
            return contentType;
        }
    }

    protected String serializeForm(Map<String, Object> formData, Charset charset) {
        StringBuilder builder = new StringBuilder();
        formData.forEach((name, value) -> {
            if (name == null) {
                Assert.isTrue(value == null, "Null name in form data: " + formData);
                return;
            }
            try {
                if (builder.length() != 0) {
                    builder.append('&');
                }
                builder.append(URLEncoder.encode(name, charset.name()));
                if (value != null) {
                    builder.append('=');
                    builder.append(URLEncoder.encode(String.valueOf(value), charset.name()));
                }
            }
            catch (UnsupportedEncodingException ex) {
                throw new IllegalStateException(ex);
            }
        });

        return builder.toString();
    }
}

