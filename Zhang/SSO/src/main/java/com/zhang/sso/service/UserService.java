package com.zhang.sso.service;


import com.zhang.common.domain.Result;
import com.zhang.common.domain.User;

/**
 * Created by Jone on 2018-01-09.
 */
public interface UserService {
    /**
     * 数据有效性检验
     * @param type 数据类型
     * @param data 数据内容
     * @return  检验结果
     */
    Result checkData(int type, String data);

    /**
     * 用户注册
     * @param user  用户相关信息
     * @return
     */
    Result register(User user);

    /**
     * 用户登录
     * @param user 用户登录相关信息
     * @return
     */

    Result login(User user);

    /**
     * 根据token查找与之对应的用户
     * @param token 相应token
     * @return
     */
    Result findByToken(String token);

    /**
     * 根据token登出
     * @param token
     * @return
     */
    Result logout(String token);
}
