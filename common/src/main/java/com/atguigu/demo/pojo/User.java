/*
 * Copyright (c) 2017, Xuliang Huang. All rights reserved.
 */

package com.atguigu.demo.pojo;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name="demo_user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer userId;
    private String userName;
    private String userPwd;

    public User() {

    }

    public User(Integer userId, String userName, String userPwd) {
        super();
        this.userId = userId;
        this.userName = userName;
        this.userPwd = userPwd;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", userName=" + userName + ", userPwd=" + userPwd + "]";
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }
}
