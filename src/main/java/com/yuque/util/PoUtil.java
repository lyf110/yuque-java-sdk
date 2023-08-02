package com.yuque.util;


import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yuque.anno.MapParams;
import com.yuque.exception.YuqueException;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 11029
 * @description 主要将PO转成MAP的工具类
 * @since 2023/8/2 10:05:42
 */
public final class PoUtil {
    private PoUtil() {
    }


    /**
     * 将实体类转成map
     *
     * @param target 实体对象
     * @return Map<String, String>
     */
    public static <T> Map<String, String> toMap(T target) throws YuqueException {
        assert ObjectUtil.isNotEmpty(target);
        Field[] declaredFields = target.getClass().getDeclaredFields();
        if (ArrayUtil.isEmpty(declaredFields)) {
            return Collections.emptyMap();
        }

        Map<String, String> map = new HashMap<>(declaredFields.length);
        String key;
        for (Field declaredField : declaredFields) {
            if (declaredField.toString().contains(" static ")) {
                continue;
            }
            MapParams mapParamsAnnotation = declaredField.getAnnotation(MapParams.class);
            if (mapParamsAnnotation != null) {
                key = mapParamsAnnotation.value();
            } else {
                key = declaredField.getName();
            }

            declaredField.setAccessible(true);
            Object obj;
            try {
                obj = declaredField.get(target);
            } catch (IllegalAccessException e) {
                throw new YuqueException(e.getMessage(), e);
            } finally {
                declaredField.setAccessible(false);
            }

            if (obj != null) {
                map.put(key, obj.toString());
            }
        }
        return map;
    }
}
