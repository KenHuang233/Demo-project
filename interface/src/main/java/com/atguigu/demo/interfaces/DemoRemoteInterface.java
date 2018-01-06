/*
 * Copyright (c) 2017, Xuliang Huang. All rights reserved.
 */

package com.atguigu.demo.interfaces;

import com.atguigu.demo.pojo.DubboResult;
import com.atguigu.demo.pojo.User;
import com.atguigu.demo.pojo.Detail;

public interface DemoRemoteInterface {

    String getMessageForTest(String param);

    DubboResult doRegistRemote(User user);

    DubboResult doLoginRemote(User user);

    DubboResult getDetailRemote(Integer userId);

    DubboResult modifyDetailRemote(Detail detail);

    DubboResult doSearchRemote(String keywords);
}
