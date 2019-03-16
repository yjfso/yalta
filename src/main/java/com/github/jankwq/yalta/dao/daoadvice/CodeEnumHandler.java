package com.github.jankwq.yalta.dao.daoadvice;

import lombok.NoArgsConstructor;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author yinjianfeng
 * @date 2018/12/17
 */
@NoArgsConstructor
public class CodeEnumHandler<T extends CodeEnum> extends BaseTypeHandler<T> {

    private T[] enums;

    public CodeEnumHandler(Class<T> type){
        if (type == null){
            throw new UnsupportedOperationException("Type cannot be null");
        }
        enums = type.getEnumConstants();
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter.getCode(), JdbcType.SMALLINT.TYPE_CODE);
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if (!rs.wasNull()){
            return byCode(rs.getInt(columnName));
        }
        return null;
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if (!rs.wasNull()){
            return byCode(rs.getInt(columnIndex));
        }
        return null;
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (!cs.wasNull()){
            return byCode(cs.getInt(columnIndex));
        }
        return null;
    }

    public T valueOf(String name) {
        for (T value: enums) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return null;
    }

    private T byCode(int code){
        if (code == 0) {
            return null;
        }
        for (T value : enums) {
            if (code == value.getCode()){
                return value;
            }
        }
        return null;
    }
}
