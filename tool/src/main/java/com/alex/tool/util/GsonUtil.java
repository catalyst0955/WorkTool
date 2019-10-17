package com.alex.tool.util;

import com.alex.tool.annotation.MergeAlias;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class GsonUtil {

//
//    public static <T> T mergeForDB(Object prime, Object other, Class<T> clazz) {
//        Gson gson = new Gson();
//        HashMap<String, String> primeMap = gson.fromJson(gson.toJson(prime), HashMap.class);
//        HashMap<String, String> otherMap = gson.fromJson(gson.toJson(other), HashMap.class);
//        Map<String, String> stringStringMap = changeFirstToUpperCase(otherMap);
//        primeMap.putAll(stringStringMap);
//        return gson.fromJson(gson.toJson(primeMap), clazz);
//    }
//
//    public static <T> T mergeForView(Object prime, Object other, Class<T> clazz) {
//
//        Gson gson = new Gson();
//        HashMap<String, String> primeMap = gson.fromJson(gson.toJson(prime), HashMap.class);
//        HashMap<String, String> otherMap = gson.fromJson(gson.toJson(other), HashMap.class);
//        Map<String, String> stringStringMap = changeFirstToLowerCase(otherMap);
//        primeMap.putAll(stringStringMap);
//        return gson.fromJson(gson.toJson(primeMap), clazz);
//    }

    public static <T> T mergeForTest(Object prime, Object other, Class<T> clazz, boolean isDb) {
        Gson gson = new Gson();
        HashMap<String, String> otherMap = gson.fromJson(gson.toJson(other), HashMap.class);
        Class<?> aClass = prime.getClass();
        for (Field declaredField : aClass.getDeclaredFields()) {
            MergeAlias annotation = declaredField.getAnnotation(MergeAlias.class);
            if (annotation != null) {
                for (String s : annotation.value()) {
                    String s1 = otherMap.get(s);
                    if (s1 != null) {
                        try {
                            Method method = aClass.getMethod("set" + changeFirstToUpperCase(declaredField.getName()), aClass);
                            method.invoke(prime, s1);
                            otherMap.remove(s);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        HashMap<String, String> primeMap = gson.fromJson(gson.toJson(prime), HashMap.class);
        Map<String, String> stringStringMap = null;
        if (isDb) {
            stringStringMap = changeFirstToUpperCase(otherMap);
        } else {
            stringStringMap = changeFirstToLowerCase(otherMap);
        }
        primeMap.putAll(stringStringMap);
        return gson.fromJson(gson.toJson(primeMap), clazz);
    }

    private static Map<String, String> changeFirstToUpperCase(HashMap<String, String> o) {
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

    private static Map<String, String> changeFirstToLowerCase(HashMap<String, String> o) {
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
