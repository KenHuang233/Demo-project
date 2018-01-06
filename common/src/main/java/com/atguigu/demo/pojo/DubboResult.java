/*
 * Copyright (c) 2017, Xuliang Huang. All rights reserved.
 */

package com.atguigu.demo.pojo;

import java.io.Serializable;

public class DubboResult implements Serializable {
    public static final String OK = "OK";
    public static final String FAILED = "FAILED";

    private static final long serialVersionUID = 1L;
    private String operationResult;
    private Serializable entity;

    public DubboResult() {

    }

    public DubboResult(String operationResult, Serializable entity) {
        super();
        this.operationResult = operationResult;
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "DubboResult [operationResult=" + operationResult + ", entity=" + entity + "]";
    }

    public String getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(String operationResult) {
        this.operationResult = operationResult;
    }

    public Serializable getEntity() {
        return entity;
    }

    public void setEntity(Serializable entity) {
        this.entity = entity;
    }

}
