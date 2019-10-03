package com.alex.tool.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.codehaus.jackson.map.ObjectMapper;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.stream.Collectors;

public class ConvertUtil {

    public String convertNullToEmptyString(Object pojo){
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.convertValue(pojo, Map.class);
        Map<String, Object> collect = map.entrySet().stream().collect(Collectors.toMap(
                e -> e.getKey(), e -> e.getValue() != null ? e.getValue() : ""
        ));
        return new Gson().toJson(collect);
    }
}
