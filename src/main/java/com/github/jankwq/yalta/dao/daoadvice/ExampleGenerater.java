package com.github.jankwq.yalta.dao.daoadvice;

import com.github.jankwq.yalta.annotation.Order;
import com.github.jankwq.yalta.annotation.Orders;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.jankwq.yalta.dao.daoadvice.SearchDto.ORDER_KEY;

/**
 * @author yinjianfeng
 * @date 2019/1/28
 */
@Slf4j
class ExampleGenerater {

    @AllArgsConstructor
    enum Compare {
        //
        equals("") {

            @Override
            public void andTo(Example.Criteria criteria, String key, Object value)  {
                criteria.andEqualTo(key, value);
            }

        },
        like("Like") {

            @Override
            public void andTo(Example.Criteria criteria, String key, Object value) {
                criteria.andLike(key, String.format("%%%s%%", value));
            }
        },
        lt("Lt") {

            @Override
            public void andTo(Example.Criteria criteria, String key, Object value) {
                criteria.andLessThan(key, value);
            }
        },
        gt("Gt") {

            @Override
            public void andTo(Example.Criteria criteria, String key, Object value) {
                criteria.andGreaterThan(key, value);
            }
        }
        ;

        private final String nameSuffix;

        public abstract void andTo(Example.Criteria criteria, String key, Object value);

        public String realName(String key) {
            return key.substring(0, key.length() - nameSuffix.length());
        }

        public static Compare fromKey(String key) {
            for (Compare value : values()) {
                if (StringUtils.isNotEmpty(value.nameSuffix) && key.endsWith(value.nameSuffix)) {
                    return value;
                }
            }
            return equals;
        }
    }

    @AllArgsConstructor
    static class QueryConditionMeta {

        protected String key;

        protected Compare compare;
    }

    static class FixedQueryConditionMeta extends QueryConditionMeta {

        private Object value;

        FixedQueryConditionMeta(String key, Compare compare, Object value) {
            super(key, compare);
            this.value = value;
        }

        void andTo(Example.Criteria criteria) {
            compare.andTo(criteria, key, value);
        }
    }

    static class DynamicQueryConditionMeta extends QueryConditionMeta {

        private CodeEnumHandler handler;

        DynamicQueryConditionMeta(String key, Compare compare, CodeEnumHandler handler) {
            super(key, compare);
            this.handler = handler;
        }

        void andTo(Example.Criteria criteria, String value) {
            compare.andTo(criteria, key, this.handler == null ? value : this.handler.valueOf(value));
        }
    }

    @AllArgsConstructor
    @Getter
    static class GenerateMeta {

        Class entity;

        List<FixedQueryConditionMeta> fixedMeta;

        Map<String, DynamicQueryConditionMeta> dynamicMeta;

        Map<String, Order.OrderType> fixedOrderMeta;

        Set<String> dynamicOrderMeta;
    }

    private final static Map<Class<?>, GenerateMeta> GENERATE_META_MAP = new HashMap<>();

    private static GenerateMeta getGenerateMeta(Class<?> parameterType) {
        GenerateMeta meta = GENERATE_META_MAP.get(parameterType);
        if (meta == null) {
            synchronized (parameterType) {
                meta = GENERATE_META_MAP.get(parameterType);
                if (meta == null) {
                    List<FixedQueryConditionMeta> fixedMetas = new LinkedList<>();
                    Map<String, DynamicQueryConditionMeta> dynamicMetas = new HashMap<>(4);
                    Object instance = null;
                    try {
                        instance = parameterType.newInstance();
                    } catch (Exception e) {
                        log.warn("[YALTA] constructor without args not found, default value will no effect");
                    }
                    Field[] fields = parameterType.getDeclaredFields();
                    for (Field field : fields) {
                        String key = field.getName();
                        Compare compare = Compare.fromKey(key);
                        String realKey = compare.realName(key);

                        Object def = null;
                        try {
                            field.setAccessible(true);
                            def = instance == null ? null : field.get(instance);
                        } catch (IllegalAccessException e) {
                        }
                        if (def != null) {
                            fixedMetas.add(
                                    new FixedQueryConditionMeta(realKey, compare, def)
                            );
                        } else {
                            CodeEnumHandler handler = null;
                            if (CodeEnum.class.isAssignableFrom(field.getType())) {
                                handler = new CodeEnumHandler(field.getType());
                            }
                            dynamicMetas.put(
                                    key,
                                    new DynamicQueryConditionMeta(realKey, compare, handler)
                            );
                        }
                    }

                    // order
                    Stream<Order> orderStream = null;
                    Orders orders = parameterType.getAnnotation(Orders.class);
                    if (orders != null) {
                        orderStream = Stream.of(orders.value());
                    } else {
                        Order order = parameterType.getAnnotation(Order.class);
                        if (order != null) {
                            orderStream = Stream.of(order);
                        }
                    }

                    meta = new GenerateMeta(
                            (Class) ((ParameterizedType)parameterType
                                    .getGenericSuperclass()).getActualTypeArguments()[0],
                            fixedMetas, dynamicMetas,
                            orderStream == null ?
                                    null : orderStream.filter(
                                            order -> order.type() != Order.OrderType.None
                                            ).collect(Collectors.toMap(Order::value, Order::type)),
                            orderStream == null ?
                                    null : orderStream.map(Order::value).collect(Collectors.toSet())
                            );
                    GENERATE_META_MAP.put(parameterType, meta);
                }
            }
        }
        return meta;
    }

    @SuppressWarnings("unchecked")
    static Example generate(Class<?> parameterType, Map<String, String[]> queryMap) {
        if (parameterType == null) {
            return null;
        }
        if (SearchDto.class.isAssignableFrom(parameterType)) {
            GenerateMeta meta = getGenerateMeta(parameterType);
            Example example = new Example(meta.entity);
            Example.Criteria criteria = example.createCriteria();
            meta.getFixedMeta().forEach(item -> item.andTo(criteria));

            Set<String> queryOrderFields = new HashSet<>(1);

            queryMap.forEach(
                    (key, values) -> {
                        if (ORDER_KEY.equals(key)) {
                            if (meta.dynamicOrderMeta != null) {
                                for (String fieldName : values) {
                                    Order.OrderType orderType;
                                    if (fieldName.startsWith("-")) {
                                        fieldName = fieldName.substring(1);
                                        orderType = Order.OrderType.desc;
                                    } else {
                                        orderType = Order.OrderType.asc;
                                    }
                                    if (meta.dynamicOrderMeta.contains(fieldName)) {
                                        queryOrderFields.add(fieldName);
                                        orderType.order(example.orderBy(fieldName));
                                    }
                                }
                            }
                        } else {
                            DynamicQueryConditionMeta dynamicMeta = meta.getDynamicMeta().get(key);
                            if (dynamicMeta != null) {
                                dynamicMeta.andTo(criteria, values[0]);
                            }
                        }
                    }
            );

            //order
            if (meta.fixedOrderMeta != null) {
                meta.fixedOrderMeta.forEach(
                        (fieldName, orderType) -> {
                            if (!queryOrderFields.contains(fieldName)) {
                                orderType.order(example.orderBy(fieldName));
                            }
                        }
                );

            }
            return example;
        }
        return null;
    }
}
