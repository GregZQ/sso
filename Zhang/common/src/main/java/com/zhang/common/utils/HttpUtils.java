package com.zhang.common.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jone on 2018-01-13.
 */
public class HttpUtils {
    private static final String PATH="http://localhost:8080/user/token";
    private static HttpClient getClient(){
        HttpClient client= HttpClients.createDefault();
        return client;
    }

    public static HttpResponse sendByGet(String msg) throws IOException {
        HttpClient httpClient=getClient();
        HttpGet get=new HttpGet(PATH+"/"+msg);
        return httpClient.execute(get);
    }

    public static String responseToString(HttpResponse response) throws IOException {
        HttpEntity entity=response.getEntity();
        if (entity==null){
            return "";
        }
        String value=streamToString(entity.getContent());
        return value;
    }

    public static String getMsgByGet(String msg) throws IOException {
        HttpResponse response=sendByGet(msg);

        return responseToString(response);
    }
    private static String streamToString(InputStream inputStream) throws IOException {
        byte bytes[]=new byte[1024];
        int i=0;
        StringBuffer stringBuffer=new StringBuffer();
        while ((i=inputStream.read(bytes))>0){
            stringBuffer.append(new String(bytes,0,i));
        }
        return stringBuffer.toString();
    }
}
