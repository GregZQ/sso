package com.zhang.sso.controller;

/**
 * Created by Jone on 2018-01-09.
 */

import com.zhang.common.domain.*;
import com.zhang.sso.service.UserService;
import com.zhang.common.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * springmvc使用.html后缀不能相应json数据
 * 可用的话会返回true，意思是数据库中没有查询到
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private String TOKEN_ID="TOKEN_ID";
    @Autowired
    @Qualifier("userService")
    private UserService userService;

    /**
     * 进行数据完整性的验证
     * @param type  验证类别
     * @param data  验证数据
     * @return  返回验证结果
     */
    @RequestMapping("/check/{type}/{data}")
    public @ResponseBody
    Result checkData(@PathVariable int type, @PathVariable String data){
        return userService.checkData(type,data);
    }

    /**
     * 进行注册
     * @param user
     * @return
     */
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public @ResponseBody Result register(User user){
        return this.userService.register(user);
    }


    /**
     * 单点登录，登录成功后会生成一个token保存在cookie当中
     * @param user  登录用户信息
     * @param request  获取登录相关数据
     * @param response 回复
     * @return 返回登录结果
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public @ResponseBody Result login(User user,HttpServletRequest request,
                                      HttpServletResponse response){

        String url=request.getParameter("url");
        //从service层获取信息，如果登陆成功里面会保存有token
        Result result= this.userService.login(user);
        //将cookie写入本地根域名下面
        if (result.getStatus().equals(200)){
            CookiteUtils.setCookie(request,response,TOKEN_ID, (String) result.getData());
            //如果url不为空，直接跳转回原来访问的页面
            if (!StringUtils.isBlank(url)){
                try {
                    response.sendRedirect(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return  result;
    }

    /**
     * 根据token验证用户是否登陆
     * @param token 登陆token
     * @param callback
     * @return
     */
    @RequestMapping(value = "/token/{token}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody String checkToken(@PathVariable String token,String callback){

        Result result=userService.findByToken(token);

        if (callback!=null&&!callback.trim().equals("")){
            return callback+"("+JsonUtils.objectToJson(result)+")";
        }
        return JsonUtils.objectToJson(result);
    }

    /**
     * 根据token登出
     * @param token  保存在cookie中的token
     * @return
     */
    @RequestMapping("/logout/{token}")
    public @ResponseBody String logout(@PathVariable  String token){

        Result result=this.userService.logout(token);
       return JsonUtils.objectToJson(result);
    }
}
