package com.github.jank.yalta.bean.updater;

import com.github.jank.yalta.bean.BaseBean;
import com.github.jank.yalta.bean.EnableBulkUpdate;
import com.github.jank.yalta.bean.Alias;
import com.github.jank.yalta.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author yinjianfeng
 * @date 2018/12/18
 */
@Slf4j
public abstract class BaseUpdater {

    /**
     * 根据具体类获取数据
     * @param name 字段名或别名
     * @return
     */
    abstract Object getValue(String name);

    public void run(BaseBean baseBean){
        List<Field> fields = ReflectUtil.getAllFields(baseBean.getClass());
        try {
            for (Field field : fields) {
                boolean enableBulkUpdate = field.isAnnotationPresent(EnableBulkUpdate.class);
                Alias alias = field.getAnnotation(Alias.class);
                if (enableBulkUpdate || alias != null){
                    String name;
                    name = alias == null ? field.getName() : alias.value();
                    Object value = getValue(name);
                    field.setAccessible(true);
                    field.set(baseBean, value);
                }
            }
        } catch (IllegalAccessException e){
            log.error("[WM] IllegalAccessException", e);
        }
    }
}
