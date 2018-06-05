package com.liepin.macaw.utils;

import javax.activation.UnsupportedDataTypeException;

public class CastUtils {

    public static <T> T cast(Object object, Class<T> tClass) throws UnsupportedDataTypeException {
        if (object == null) {
            return null;
        } else if (tClass.isInstance(object)) {
            return tClass.cast(object);
        } else if (tClass == String.class) {
            return (T) object.toString();
        } else if (tClass == Integer.class) {
            if (object instanceof Number) {
                return (T)Integer.valueOf(((Number) object).intValue());
            }
            return (T)Integer.valueOf(object.toString());
        }else if (tClass == Double.class) {
            if (object instanceof Number) {
                return (T)Double.valueOf(((Number) object).doubleValue());
            }
            return (T)Double.valueOf(object.toString());
        }else if (tClass == Long.class) {
            if (object instanceof Number) {
                return (T)Long.valueOf(((Number) object).longValue());
            }
            return (T)Long.valueOf(object.toString());
        }else if (tClass == Float.class) {
            if (object instanceof Number) {
                return (T)Float.valueOf(((Number) object).floatValue());
            }
            return (T)Float.valueOf(object.toString());
        }else if (tClass == Short.class) {
            if (object instanceof Number) {
                return (T)Short.valueOf(((Number) object).shortValue());
            }
            return (T)Short.valueOf(object.toString());
        }
        throw new UnsupportedDataTypeException("不支持的数据类型！" + tClass.getName());
    }

}
