package com.zhang.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 到相关页面
 */
@Controller
public class IndexController {
/*    //进入登录界面
    @RequestMapping(value = "/login")
    public String login(){
        return "login";
    }*/

    @RequestMapping(value = "/login")
    public String login(String url, Model model){
        model.addAttribute("url",url);
        return "login";
    }

   //进入注册界面
    @RequestMapping("/register")
    public String register(){
        return "register";
    }
}
