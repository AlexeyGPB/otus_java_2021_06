package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

import static java.lang.Math.max;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    private final EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }


    @Override
    public String getSelectAllSql() {
        return String.format("SELECT * FROM %s", entityClassMetaData.getName());
    }


    @Override
    public String getSelectByIdSql() {
        return String.format("SELECT %s FROM %s WHERE %s = ?",
                entityClassMetaData.getAllFields().stream().map(Field::getName).collect(Collectors.joining(", ")),
                entityClassMetaData.getName(),
                entityClassMetaData.getIdField().getName());
    }



    @Override
    public String getInsertSql() {
        return String.format("INSERT INTO %s(%s) VALUES (%s)",
                entityClassMetaData.getName(),
                entityClassMetaData.getFieldsWithoutId().stream().map(Field::getName).collect(Collectors.joining(", ")),
                "?" + ", ?".repeat(max(0, entityClassMetaData.getFieldsWithoutId().size() - 1)));
    }


    @Override
    public String getUpdateSql() {
        return String.format("UPDATE %s SET %s WHERE %s = ?",
                entityClassMetaData.getName(),
                entityClassMetaData.getFieldsWithoutId().stream().map(c -> c.getName() + " = ?").collect(Collectors.joining(",")),
                entityClassMetaData.getIdField().getName());
    }

}
