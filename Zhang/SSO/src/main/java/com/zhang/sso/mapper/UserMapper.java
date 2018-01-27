package com.zhang.sso.mapper;


import com.zhang.common.domain.User;

import java.util.List;

/**
 * Created by Jone on 2018-01-09.
 */
public interface UserMapper {
    /**
     * 根据相关信息查找用户
     * @param user
     * @return
     */
    List<User> findByExample(User user);

    /**
     * 用户插入
     * @param user
     */
    void insert(User user);

    /**
     * 根据账户名密码查询用户
     * @param user
     * @return
     */
    User findByUsernameAndPassword(User user);
}
