/*
 * Copyright (c) 2018, Xuliang Huang. All rights reserved.
 */

package com.atguigu.demo.utils;

import com.google.gson.Gson;

public class GsonUtils {
    public static String convertPojoOrMapToJson(Object pojoOrMap) {

        return new Gson().toJson(pojoOrMap);
    }

    public static <T> T convertJsonToPojoOrMap(String json, Class<T> clazz) {

        return new Gson().fromJson(json, clazz);
    }
}
