package com.github.jank.yalta.bean.updater;

import com.github.jank.yalta.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author yinjianfeng
 * @date 2018/12/18
 */
@Slf4j
public class ObjectUpdater extends BaseUpdater {

    private final Map<String, Field> objFields;

    private final Object object;

    public ObjectUpdater(Object object){
        this.object = object;
        objFields = ReflectUtil.getAllFieldMap(object.getClass());
    }

    @Override
    Object getValue(String name) {
        if (objFields.containsKey(name)){
            try{
                Field field = objFields.get(name);
                field.setAccessible(true);
                return field.get(object);
            } catch (IllegalAccessException e){
                log.error("[WM] IllegalAccessException", e);
            }
        }
        return null;
    }
}
