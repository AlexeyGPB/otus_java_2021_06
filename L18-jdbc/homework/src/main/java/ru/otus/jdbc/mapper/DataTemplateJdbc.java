package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return createNewInstance(rs);
                }
                return null;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            var clientList = new ArrayList<T>();
            try {
                while (rs.next()) {
                    clientList.add(createNewInstance(rs));
                }
                return clientList;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T object) {
        try {
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), getNewValue(object));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T object) {
        try {
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), getNewValue(object));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private T createNewInstance(ResultSet rs) throws Exception {
        var instance = entityClassMetaData.getConstructor().newInstance();
        setFields(rs, instance, entityClassMetaData.getAllFields());
        return (T) instance;
    }

    private void setFields(ResultSet rs, Object instance, List<Field> fields) throws SQLException {
        for (Field field : fields) {
            setFieldValue(instance, field, rs.getObject(field.getName()));
        }
    }

    private void setFieldValue(Object instance, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new DataTemplateException(e);
        }
    }

    private List<Object> getNewValue(T object) throws IllegalAccessException {
        List<Field> fields = entityClassMetaData.getFieldsWithoutId();
        var values = new ArrayList<>(fields.size());
        for (var field : fields) {
            field.setAccessible(true);
            values.add(field.get(object));
        }
       return values;
    }
  }


