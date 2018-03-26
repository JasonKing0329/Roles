package com.king.app.roles.model.entity;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/26 11:08
 */
public class RoleRacesConverter implements PropertyConverter {
    @Override
    public List<Race> convertToEntityProperty(Object databaseValue) {
        String value = (String) databaseValue;
        return null;
    }

    @Override
    public String convertToDatabaseValue(Object entityProperty) {
        return null;
    }
}
