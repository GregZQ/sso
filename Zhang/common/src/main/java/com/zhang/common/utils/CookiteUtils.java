package com.zhang.common.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 用户将token保存到根域名下
 */
public class CookiteUtils {

    /**
     * 得到cookite，不编码
     */
    public static String getCookieValue(HttpServletRequest request,String name){
        return  getCookieValue(request,name,false);
    }

    /**
     * 得到cooikte，编码
     * @param request
     * @param name
     * @param flag
     * @return
     */
    public static String getCookieValue(HttpServletRequest request,String name,boolean flag){
        Cookie[] cookList=request.getCookies();
        if (cookList==null||name==null){
            return null;
        }
        String retValue=null;
        try {
            for (int i=0;i<cookList.length;i++){
                if (cookList[i].getName().equals(name)){
                    if (flag){
                        retValue= URLDecoder.decode(cookList[i].getValue(),"UTF-8");
                    }else{
                        retValue=cookList[i].getValue();
                    }
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  retValue;
    }
    /**
     * 指定cookite编码
     */
    public static String getCookieValue(HttpServletRequest request,String name,String encoding){
        Cookie[] cookieList=request.getCookies();
        if (cookieList==null||name==null){
            return null;
        }
        String val=null;
        try {
            for (int i = 0; i < cookieList.length; i++) {
                if (cookieList[i].getName().equals(name)) {
                    val=URLDecoder.decode(cookieList[i].getValue(), encoding);
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return val;
    }
    /**
     * 设置cookie的值,关闭浏览器立即失败
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response,
                                 String cookieName,String cookieValue){
        setCookie(request,response,cookieName,cookieValue,-1);
    }

    public static void setCookie(HttpServletRequest request,HttpServletResponse response,
                                String cookieName,String cookiteValue,int time){
        setCookie(request,response,cookieName,cookiteValue,time,false);
    }

    public static void setCookie(HttpServletRequest request,HttpServletResponse response,
                           String cookieName,String cookieValue,int time,boolean isEncoding){
        doSetCookie(request,response,cookieName,cookieValue,time,isEncoding);
    }
    public static void setCookie(HttpServletRequest request,HttpServletResponse response,
                                 String cookieName,String cookieValue,int time,String encoding){
        doSetCookie(request,response,cookieName,cookieValue,time,encoding);
    }

    private static final void doSetCookie(HttpServletRequest request,HttpServletResponse response,
                             String cookieName,String cookieValue,int time,boolean isEncoding){
           try{
               if (cookieValue==null)
               cookieValue="";
               else if(isEncoding){
                   cookieValue= URLEncoder.encode(cookieValue,"UTF-8");
               }
               Cookie cookie=new Cookie(cookieName,cookieValue);
               if (time>0){
                   cookie.setMaxAge(time);
               }
               if (null!=request){//设置域名cookie
                   String domainName=getDomainName(request);
                   if (!"localhost".equals(domainName)){
                       cookie.setDomain(domainName);
                   }
               }
               cookie.setPath("/");
               response.addCookie(cookie);
           }catch (Exception e){
               e.printStackTrace();
           }
    }
    /**
     * 删除Cookie带cookie域名
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response,
                                    String cookieName) {
        doSetCookie(request, response, cookieName, "", -1, false);
    }
    private static final void doSetCookie(HttpServletRequest request, HttpServletResponse response,
                                          String cookieName, String cookieValue, int cookieMaxage, String encodeString) {
        try {
            if (cookieValue == null) {
                cookieValue = "";
            } else {
                cookieValue = URLEncoder.encode(cookieValue, encodeString);
            }
            Cookie cookie = new Cookie(cookieName, cookieValue);
            if (cookieMaxage > 0)
                cookie.setMaxAge(cookieMaxage);
            if (null != request) {// 设置域名的cookie
                String domainName = getDomainName(request);
                System.out.println(domainName);
                if (!"localhost".equals(domainName)) {
                    cookie.setDomain(domainName);
                }
            }
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //截取域名
    private static final String getDomainName(HttpServletRequest request){
        String domainName=null;

        String serverName=request.getRequestURL().toString();
        if (serverName==null||serverName.equals("")){
            domainName="";
        }else{
            serverName=serverName.toLowerCase();
            serverName=serverName.substring(7);
            //取出域名前面的所有东西,除掉第一个/后面的东西
            final int end=serverName.indexOf("/");
            serverName=serverName.substring(0,end);
            final String []domains=serverName.split("\\.");
            int length=domains.length;

            if (length>3){
                //www.xxx.com.cn，取后面三个
                domainName="."+domains[length-3]+"."+domains[length-2]+"."
                        +domains[length-1];
            }else if (length<=3&&length>1){
                //取后面的两个
                domainName="."+domains[length-2]+"."+domains[length-1];
            }else {
                domainName=serverName;
            }
        }
        //取出端口号
        if (domainName!=null&&domainName.indexOf(":")>0){
            String [] ary=domainName.split("\\:");
            return ary[0];
        }
        return domainName;
    }
}
