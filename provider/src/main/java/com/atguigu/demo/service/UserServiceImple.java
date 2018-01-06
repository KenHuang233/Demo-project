/*
 * Copyright (c) 2017, Xuliang Huang. All rights reserved.
 */

package com.atguigu.demo.service;

import com.atguigu.demo.mappers.UserDetailMapper;
import com.atguigu.demo.mappers.UserMapper;
import com.atguigu.demo.pojo.Detail;
import com.atguigu.demo.pojo.User;
import com.github.abel533.entity.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImple implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserDetailMapper detailMapper;

    @Override
    public boolean checkUserNameExists(String userName) {

        Example example = new Example(User.class);

        example.createCriteria().andEqualTo("userName", userName);

        int count = userMapper.selectCountByExample(example);

        return (count >= 1);
    }

    @Override
    public void saveUser(User user) {

        userMapper.insert(user);
//        int a = 10 / 0;
    }

    @Override
    public User getLoginUser(User user) {

        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userName", user.getUserName()).andEqualTo("userPwd", user.getUserPwd());

        List<User> list = userMapper.selectByExample(example);

        if(list != null && list.size() > 0) {
            return list.get(0);
        }else{
            return null;
        }

    }

    @Override
    public Detail getDetail(Integer userId) {

        Example example = new Example(Detail.class);
        example.createCriteria().andEqualTo("userId", userId);

        List<Detail> list = detailMapper.selectByExample(example);

        if(list == null || list.size() == 0) {
            return null;
        }

        return list.get(0);
    }

    @Override
    public void saveOrUpdateDetail(Detail detail) {

        Integer detailId = detail.getDetailId();

        //由于detailId是主键，所以detailId如果为null则说明以前没有对应的记录
        if(detailId == null) {

            //执行保存操作
            detailMapper.insert(detail);

        }else{

            //如果detailId不为null，说明以前保存过对应的记录，执行更新操作
            detailMapper.updateByPrimaryKeySelective(detail);

        }

    }

}
