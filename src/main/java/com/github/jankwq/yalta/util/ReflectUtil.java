package com.github.jankwq.yalta.util;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author yinjianfeng
 * @date 2018/11/13
 */
public class ReflectUtil {

    public static List<Field> getAllFields(Class clazz){
        List<Field> fields = new ArrayList<>();
        while (clazz != null) {
            Collections.addAll(fields, clazz.getDeclaredFields());
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    public static Map<String, Field> getAllFieldMap(Class clazz){
        Map<String, Field> fieldMap = new HashMap<>();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                fieldMap.put(field.getName(), field);
            }
            clazz = clazz.getSuperclass();
        }
        return fieldMap;
    }
}
