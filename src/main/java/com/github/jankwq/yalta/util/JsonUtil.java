package com.github.jankwq.yalta.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yinjianfeng
 * @date 2018/12/17
 */
@Slf4j
public class JsonUtil {

    private static class ObjectMapperHolder {

        private static final ObjectMapper INSTANCE = new ObjectMapper();

        static {
            INSTANCE.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        }
    }

    public static String serialize(Object value, Class view){
        try{
            return ObjectMapperHolder.INSTANCE.writerWithView(view).writeValueAsString(value);
        } catch (JsonProcessingException e){
            log.error("write json value catch error", e);
        }
        return null;
    }
}
