package com.example.demo.common.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

/**
 * @author fg
 * @date 2020/11/5
 */
@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer, WebMvcRegistrations {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    /**
     * 修改默认的JACKSON转换器，防范来自RequestBody的XSS攻击
     *
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        for (HttpMessageConverter<?> next : converters) {
            if (next instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter jsonConverter = (MappingJackson2HttpMessageConverter) next;

                SimpleModule module = new SimpleModule();
                module.addDeserializer(String.class, new JsonHtmlXssDeserializer(String.class));
                jsonConverter.getObjectMapper().registerModule(module);

                Set<Object> modules2 = jsonConverter.getObjectMapper().getRegisteredModuleIds();
                log.info("解析器现在有：{}个", modules2.size());
                break;
            }
        }
    }
}