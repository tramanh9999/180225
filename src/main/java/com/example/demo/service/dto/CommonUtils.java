package com.example.demo.service.dto;

import java.lang.reflect.Field;

public class CommonUtils {
    public static Class<?> getTypeOfField(Class<?> clazz, String fieldName) {
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                return field.getType();
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass(); // Check parent class if field is not found
            }
        }
        throw new IllegalArgumentException(
                "Field '" + fieldName + "' not found in class hierarchy.");
    }
}
