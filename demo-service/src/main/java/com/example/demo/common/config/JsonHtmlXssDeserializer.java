package com.example.demo.common.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;

/**
 * 自定义防范XSS攻击的@RequestBody的解析器
 *
 * @author fg
 * @date 2020/11/5
 */
@Slf4j
public class JsonHtmlXssDeserializer extends JsonDeserializer<String> {

    public JsonHtmlXssDeserializer(Class<String> string) {
        super();
    }

    @Override
    public Class<String> handledType() {
        return String.class;
    }

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        String value = jsonParser.getValueAsString();
        if (value != null) {
            return StringEscapeUtils.escapeHtml4(value);
        }
        return null;
    }
}
