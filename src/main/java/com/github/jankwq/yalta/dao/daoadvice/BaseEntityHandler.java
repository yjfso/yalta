package com.github.jankwq.yalta.dao.daoadvice;

import com.github.jankwq.yalta.bean.BaseEntity;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author yinjianfeng
 * @date 2018/12/17
 */
@MappedTypes(BaseEntity.class)
public class BaseEntityHandler<T extends BaseEntity> extends BaseTypeHandler<T> {

    private final List<ResultMapping> resultMappings;
    private final Configuration configuration;
    private final Class<T> entityClass;

    public BaseEntityHandler(List<ResultMapping> resultMappings, Configuration configuration, Class entityClass) {
        this.resultMappings = resultMappings;
        this.configuration = configuration;
        this.entityClass = entityClass;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter.getId(), JdbcType.SMALLINT.TYPE_CODE);
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        try {

            Object object = configuration.getObjectFactory().create(entityClass);
            final MetaObject metaObject = configuration.newMetaObject(object);
            for (ResultMapping propertyMapping : resultMappings) {
                Object value = propertyMapping.getTypeHandler().getResult(rs, propertyMapping.getColumn());
                metaObject.setValue(propertyMapping.getProperty(), value);
            }
            return (T)object;
        } catch (Exception e) {
            ;
        }
        rs.getInt(columnName);
        return null;
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {

        return null;
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {

        return null;
    }

}
