package com.alex.tool.util;

import com.alex.tool.annotation.DateFormat;
import com.alex.tool.annotation.MergeAlias;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class GsonUtil {


    public static <T> T merge(Object prime, Object other, Class<T> clazz, boolean isDb) {
        Gson gson = new Gson();
        HashMap<String, Object> otherMap = gson.fromJson(gson.toJson(other), HashMap.class);
        HashMap<String, Object> primeMap = gson.fromJson(gson.toJson(prime), HashMap.class);
        Class<?> aClass = prime.getClass();
        for (Field declaredField : aClass.getDeclaredFields()) {
            MergeAlias annotation = declaredField.getAnnotation(MergeAlias.class);
            if (annotation != null) {
                for (String s : annotation.value()) {
                    Object s1 = otherMap.get(s);
                    if (s1 != null) {
                        try {
                            if ( !annotation.dateToString().equals("")) {
                                //other 的 date filed merg 進prime的string field
                                String patteren = annotation.dateToString();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(patteren);
                                Method method = other.getClass().getMethod("get" + changeFirstToUpperCase(s), null);
                                Object invoke = method.invoke(other);
                                s1 = simpleDateFormat.format(invoke);

                            } else if (!annotation.stringToDate().equals("")) {
                                //other 的 String filed merg 進prime的Date field
                                String pattern = annotation.stringToDate();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                                Date d = simpleDateFormat.parse(String.valueOf(s1));
                                SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
                                s1 = sdf.format(d);
                            }
                                primeMap.put(declaredField.getName(), s1);
                                otherMap.remove(s);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }

        Map<String, Object> stringStringMap;
        if (isDb) {
            stringStringMap = changeFirstToUpperCase(otherMap);
        } else {
            stringStringMap = changeFirstToLowerCase(otherMap);
        }
        primeMap.putAll(stringStringMap);
        return gson.fromJson(gson.toJson(primeMap), clazz);
    }


    private static Map<String, Object> changeFirstToUpperCase(HashMap<String, Object> o) {
        return o.entrySet().stream().collect(Collectors.toMap(
                entry -> {
                    String key = String.valueOf(entry.getKey());
                    return changeFirstToUpperCase(key);
                },
                entry -> entry.getValue()));
    }

    private static String changeFirstToUpperCase(String key) {
        return key.substring(0, 1).toUpperCase() + key.substring(1);
    }

    private static Map<String, Object> changeFirstToLowerCase(HashMap<String, Object> o) {
        return o.entrySet().stream().collect(Collectors.toMap(
                entry -> {
                    String key = String.valueOf(entry.getKey());
                    return changeFirstToLowerCase(key);
                },
                entry -> entry.getValue()));
    }

    private static String changeFirstToLowerCase(String key) {
        return key.substring(0, 1).toLowerCase() + key.substring(1);
    }

}
