package com.example.demo.utils;

import java.lang.reflect.Field;

/**
 * @Author: fg
 * @Date: 2020/11/20
 */
public class ReflectUtil<T> {
    /**
     * 设置对象的字符串类型如果为null值的全部转换为""
     */
    public T updateStr(T t) {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == String.class) {

                field.setAccessible(true);
                try {
                    Object object = field.get(t);
                    if (null == object) {
                        field.set(t, "");
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return t;
    }

    /**
     * 设置对象Integer类型的属性如果为null则全部转换为0
     */
    public T updateInt(T t) {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == Integer.class) {

                field.setAccessible(true);
                try {
                    Object object = field.get(t);
                    if (null == object) {
                        field.set(t, 0);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return t;
    }
}
