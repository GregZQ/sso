package com.zhang.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * Created by Jone on 2018-01-10.
 */
/*
* Json工具包
* */
public class JsonUtils {
    private static final ObjectMapper  MAPPER=new ObjectMapper();
    public static String objectToJson(Object object){
        try {
            String string=MAPPER.writeValueAsString(object);
            return string;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T jsonToObject(String jsonData,Class<T>type){
        try {
            T t=MAPPER.readValue(jsonData,type);
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType){
        JavaType javaType=MAPPER.getTypeFactory().constructParametricType(List.class,beanType);
        try {
            List<T> list=MAPPER.readValue(jsonData,javaType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
