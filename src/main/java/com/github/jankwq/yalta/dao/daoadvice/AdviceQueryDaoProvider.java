package com.github.jankwq.yalta.dao.daoadvice;

import com.github.jankwq.yalta.annotation.One;
import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.reflection.MetaObject;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.util.MetaObjectUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yinjianfeng
 * @date 2019/1/25
 */
@Slf4j
public class AdviceQueryDaoProvider extends MapperTemplate {

    public AdviceQueryDaoProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String adviceSelect(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        //将返回值修改为实体类型
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder("SELECT ");
        if(isCheckExampleEntityClass()){
            sql.append(SqlHelper.exampleCheck(entityClass));
        }
        sql.append("<if test=\"distinct\">distinct</if>");
        //支持查询指定列
        sql.append(SqlHelper.exampleSelectColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.exampleWhereClause());
        sql.append(SqlHelper.exampleOrderBy(entityClass));
        sql.append(SqlHelper.exampleForUpdate());
        return sql.toString();
    }

    public String adviceLeftJoinSelect(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);

        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        ResultMap resultMap = entityTable.getResultMap(ms.getConfiguration());
        List<ResultMapping> mappings = new ArrayList<>(resultMap.getPropertyResultMappings());

        StringBuilder sql = new StringBuilder("SELECT ");
        if(isCheckExampleEntityClass()){
            sql.append(SqlHelper.exampleCheck(entityClass));
        }
        //支持查询指定列
        sql.append(" * ");
        String mainTableName = tableName(entityClass);
        sql.append(SqlHelper.fromTable(entityClass, mainTableName));

        for (Field field : entityClass.getDeclaredFields()) {
            One one = field.getAnnotation(One.class);
            if (one != null) {
                String keyName = one.foreign();
                if ("".equals(keyName)) {
                    keyName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, field.getName()) + "_id";
                }
                String tableName = tableName(field.getType());
                Class type = field.getType();
                EntityTable typeEntityTable = EntityHelper.getEntityTable(type);
                ResultMap map = typeEntityTable.getResultMap(ms.getConfiguration());
//                mappings.forEach(
//                        mapping -> {
//                            try {
//                                Field column = ResultMapping.class.getDeclaredField("column");
//                                column.setAccessible(true);
//                                column.set(mapping, mainTableName + "." + mapping.getColumn());
//                            } catch (Exception e) {
//                                throw new RuntimeException(e);
//                            }
//                        }
//                );
//                map.getResultMappings().forEach(
//                        mapping -> {
//                            try {
//                                Field column = ResultMapping.class.getDeclaredField("column");
//                                column.setAccessible(true);
//                                column.set(mapping, tableName + "." + mapping.getColumn());
//                            } catch (Exception e) {
//                                throw new RuntimeException(e);
//                            }
//                        }
//                );
                List<ResultMapping> resultMappings = map.getResultMappings().stream().map(
                        mapping ->
                                new ResultMapping
                                        .Builder(
                                                ms.getConfiguration(), mapping.getProperty(),
                                                tableName + "." + mapping.getColumn(), mapping.getTypeHandler()
                                        )
                                        .javaType(mapping.getJavaType())
                                        .jdbcType(mapping.getJdbcType())
                                        .nestedResultMapId(mapping.getNestedResultMapId())
                                        .nestedQueryId(mapping.getNestedQueryId())
                                        .build()
                ).filter(Objects::nonNull).collect(Collectors.toList());

                mappings.add(
                        new ResultMapping.Builder(ms.getConfiguration(), field.getName())
                                .composites(resultMappings)
                                .typeHandler(
                                        new BaseEntityHandler<>(resultMappings, ms.getConfiguration(), field.getType())
                                )
                                .build()
                );

                sql.append(" LEFT JOIN ").append(tableName)
                        .append(" ON ")
                        .append(mainTableName).append(".").append(keyName)
                        .append("=").append(tableName).append(".").append("id")
                        .append(" ");
            }
        }

        sql.append(SqlHelper.exampleWhereClause());
        sql.append(SqlHelper.exampleOrderBy(entityClass));
        sql.append(SqlHelper.exampleForUpdate());

        try {
            Field field = ResultMap.class.getDeclaredField("propertyResultMappings");
            field.setAccessible(true);
            field.set(resultMap, Collections.unmodifiableList(mappings));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        MetaObject metaObject = MetaObjectUtil.forObject(ms);
        metaObject.setValue("resultMaps", Collections.unmodifiableList(Collections.singletonList(resultMap)));
        return sql.toString();
    }

}
