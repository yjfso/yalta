package com.github.jank.yalta.dao.daoadvice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author yinjianfeng
 * @date 2019/1/28
 */
@Slf4j
public class ExampleGenerater {

    @AllArgsConstructor
    enum Compare {
        //
        equals("") {

            @Override
            public String[] conditionKeys(QueryMeta queryMeta) {
                return queryMeta.equals();
            }

            @Override
            public void andTo(Example.Criteria criteria, String key, Object value)  {
                criteria.andEqualTo(key, value);
            }

        },
        like("Like") {

            @Override
            public String[] conditionKeys(QueryMeta queryMeta) {
                return queryMeta.like();
            }

            @Override
            public void andTo(Example.Criteria criteria, String key, Object value) {
                criteria.andLike(key, String.format("%%%s%%", value));
            }
        },
        lt("Lt") {

            @Override
            public String[] conditionKeys(QueryMeta queryMeta) {
                return queryMeta.lt();
            }


            @Override
            public void andTo(Example.Criteria criteria, String key, Object value) {
                criteria.andLessThan(key, value);
            }
        },
        gt("Gt") {

            @Override
            public String[] conditionKeys(QueryMeta queryMeta) {
                return queryMeta.gt();
            }

            @Override
            public void andTo(Example.Criteria criteria, String key, Object value) {
                criteria.andGreaterThan(key, value);
            }
        }
        ;

        private final String nameSuffix;

        public abstract String[] conditionKeys(QueryMeta queryMeta);

        public abstract void andTo(Example.Criteria criteria, String key, Object value);

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

    @Getter
    @AllArgsConstructor
    static class OrderMeta {

        private String name;

        private boolean desc = false;

        public static List<OrderMeta> from(QueryMeta queryMeta){
            List<OrderMeta> orderMetas = new LinkedList<>();
            for (String key : queryMeta.order()) {
                OrderMeta orderMeta;
                if (key.startsWith("~")) {
                    orderMeta = new OrderMeta(key.substring(1), true);
                } else {
                    orderMeta = new OrderMeta(key, false);
                }
                orderMetas.add(orderMeta);
            }
            return orderMetas;
        }
    }

    @AllArgsConstructor
    @Getter
    static class GenerateMeta {

        List<FixedQueryConditionMeta> fixedMeta;

        Map<String, DynamicQueryConditionMeta> dynamicMeta;

        List<OrderMeta> orderMetas;

    }

    private final static Map<QueryMeta, GenerateMeta> GENERATE_META_MAP = new HashMap<>();

    @SuppressWarnings("unchecked")
    private static CodeEnumHandler getHandler(Class clazz, String key) {
        try{
            Class type = clazz.getDeclaredField(key).getType();
            if (CodeEnum.class.isAssignableFrom(type)) {
                return new CodeEnumHandler<>(type);
            }
        } catch (Exception e) {
            log.warn("[WMP] key:{} may not exist in class:{}", key, clazz);
        }
        return null;
    }

    private static GenerateMeta getGenerateMeta(QueryMeta queryMeta) {
        if (queryMeta == null) {
            return null;
        }
        GenerateMeta meta = GENERATE_META_MAP.get(queryMeta);
        if (meta == null) {
            synchronized (queryMeta) {
                meta = GENERATE_META_MAP.get(queryMeta);
                if (meta == null) {
                    List<FixedQueryConditionMeta> fixedMetas = new LinkedList<>();
                    Map<String, DynamicQueryConditionMeta> dynamicMetas = new HashMap<>();
                    for (Compare compare : Compare.values()) {
                        String[] keys = compare.conditionKeys(queryMeta);
                        if (keys != null) {
                            for (String key : keys) {
                                if (key.contains("=")) {
                                    String[] item = key.split("=");
                                    CodeEnumHandler handler = getHandler(queryMeta.value(), item[0]);
                                    fixedMetas.add(new FixedQueryConditionMeta(
                                            item[0], compare, handler == null ? item[1] : handler.valueOf(item[1])));
                                } else {
                                    CodeEnumHandler handler = getHandler(queryMeta.value(), key);
                                    dynamicMetas.put(
                                            key + compare.nameSuffix,
                                            new DynamicQueryConditionMeta(key, compare, handler)
                                    );
                                }

                            }
                        }
                    }

                    meta = new GenerateMeta(fixedMetas, dynamicMetas, OrderMeta.from(queryMeta));
                    GENERATE_META_MAP.put(queryMeta, meta);
                }
            }
        }
        return meta;
    }

    @SuppressWarnings("unchecked")
    static Example generate(QueryMeta queryMeta, Map<String, String[]> queryMap) {
        Example example = new Example(queryMeta.value());
        Example.Criteria criteria = example.createCriteria();
        GenerateMeta meta = getGenerateMeta(queryMeta);

        meta.getFixedMeta().forEach(item -> item.andTo(criteria));

        queryMap.forEach(
                (key, values) -> {
                    DynamicQueryConditionMeta dynamicMeta = meta.getDynamicMeta().get(key);
                    if (dynamicMeta != null) {
                        dynamicMeta.andTo(criteria, values[0]);
                    }
                }
        );

        meta.getOrderMetas().forEach(
                orderMeta -> {
                    Example.OrderBy orderBy = example.orderBy(orderMeta.name);
                    if (orderMeta.desc) {
                        orderBy.desc();
                    }
                }
        );

        return example;
    }
}
