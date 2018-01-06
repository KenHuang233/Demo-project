/*
 * Copyright (c) 2017, Xuliang Huang. All rights reserved.
 */

package com.atguigu.demo.service;

import com.atguigu.demo.pojo.User;

import com.atguigu.demo.pojo.Detail;

public interface UserService {
    boolean checkUserNameExists(String userName);

    void saveUser(User user);

    User getLoginUser(User user);

    Detail getDetail(Integer userId);

    void saveOrUpdateDetail(Detail detail);
}
