package com.zhang.webpage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Jone on 2018-01-13.
 */
@Controller
public class IndexController {


    @RequestMapping("/index")
    public String index(){
        return  "index";
    }

}
