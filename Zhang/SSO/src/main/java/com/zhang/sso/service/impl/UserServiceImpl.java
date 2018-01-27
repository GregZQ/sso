package com.zhang.sso.service.impl;


import com.zhang.common.domain.Result;
import com.zhang.common.domain.User;
import com.zhang.common.utils.JedisUtils;
import com.zhang.common.utils.JsonUtils;
import com.zhang.common.utils.StringUtils;
import com.zhang.sso.mapper.UserMapper;
import com.zhang.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.UUID;

/**
 * Created by Jone on 2018-01-09.
 */
@Service
@Qualifier("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    private Jedis jedis= JedisUtils.getJedis();

    private String TOKEN_ID="TOKEN_ID";


    public Result checkData(int type, String data) {
        //判断传入信息的有效性
        User user=new User();
        if (type==1){
            user.setUsername(data);
        }else if (type==2){
            user.setPhone(data);
        }else if (type==3){
            user.setEmail(data);
        }else{
            //输入信息错误，并保存错误信息
            return Result.build(400,"输入非法");
        }
        List<User> list= userMapper.findByExample(user);

        if (list!=null&&list.size()>0){
            return Result.ok(false);
        }
        return Result.ok(true);
    }

    public Result register(User user){
        if (StringUtils.isBlank(user.getUsername())|| StringUtils.isBlank(user.getEmail())||
            StringUtils.isBlank(user.getPhone())){
            return  Result.build(400,"输入信息不能为空");
        }
        if (false ==((Boolean)(checkData(1, user.getUsername()).getData()))){
            return  Result.build(400,"用户名不能重复");
        }
        if (false ==((Boolean)(checkData(2, user.getPhone()).getData()))){
            return  Result.build(400,"手机号不能重复");
        }
        if (false == ((Boolean)(checkData(3, user.getEmail()).getData()))){
            return  Result.build(400,"邮箱不能重复");
        }
        //注册之后将信息加密放到数据库中
        String value=DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(value);
        userMapper.insert(user);
        return Result.ok(true);
    }


    public Result login(User user){
        //先检验用户名与密码是否为空
        if (StringUtils.isBlank(user.getUsername())||
            StringUtils.isBlank(user.getPassword())){
            return Result.build(400,"用户名或密码不能为空");
        }
        //转为UUID与数据库中的用户名密码进行比较
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        //根据用户的用户名密码查询信息
        User temp=userMapper.findByUsernameAndPassword(user);

        //如果用户名密码正确会查询出信息，不对的话无法查询到
        if(temp==null){
            return Result.build(400,"用户名或密码不正确");
        }
        //查询到的话生成token作为键，用户的信息作为值保存到redis中
        String token_ID=UUID.randomUUID().toString().replaceAll("-","");
        temp.setPassword(null);
        jedis.set(TOKEN_ID+":"+token_ID, JsonUtils.objectToJson(temp));
        jedis.expire(TOKEN_ID+":"+token_ID,300);
        //返回给controller层，并从controller层中将token保存到根cookie中
        return Result.ok(token_ID);
    }

    public Result findByToken(String token) {
        String value=jedis.get(TOKEN_ID+":"+token);

        if (value==null){
            return Result.build(400,"登陆超时");
        }

        jedis.expire(TOKEN_ID+":"+token,300);
        return Result.ok(JsonUtils.jsonToObject(value,User.class));
    }

    public Result logout(String token) {
        long i=jedis.del(TOKEN_ID+":"+token);
        if (i>0){
            return Result.ok("注销成功");
        }
        return Result.build(400,"注销失败");
    }
}
