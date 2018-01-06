/*
 * Copyright (c) 2018, Xuliang Huang. All rights reserved.
 */

package com.atguigu.demo.pojo;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name="demo_detail")
public class Detail implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer detailId;
    private String userNick;
    private Integer userGender;
    private String userJob;
    private String userHometown;
    private String userDesc;
    private String userPicGroup;
    private String userPicFileName;
    private Integer userId;

    public Detail() {

    }

    public Detail(Integer detailId, String userNick, Integer userGender, String userJob, String userHometown,
                  String userDesc, String userPicGroup, String userPicFileName, Integer userId) {
        super();
        this.detailId = detailId;
        this.userNick = userNick;
        this.userGender = userGender;
        this.userJob = userJob;
        this.userHometown = userHometown;
        this.userDesc = userDesc;
        this.userPicGroup = userPicGroup;
        this.userPicFileName = userPicFileName;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Detail [detailId=" + detailId + ", userNick=" + userNick + ", userGender=" + userGender + ", userJob="
                + userJob + ", userHometown=" + userHometown + ", userDesc=" + userDesc + ", userPicGroup="
                + userPicGroup + ", userPicFileName=" + userPicFileName + ", userId=" + userId + "]";
    }

    public Integer getDetailId() {
        return detailId;
    }

    public void setDetailId(Integer detailId) {
        this.detailId = detailId;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public Integer getUserGender() {
        return userGender;
    }

    public void setUserGender(Integer userGender) {
        this.userGender = userGender;
    }

    public String getUserJob() {
        return userJob;
    }

    public void setUserJob(String userJob) {
        this.userJob = userJob;
    }

    public String getUserHometown() {
        return userHometown;
    }

    public void setUserHometown(String userHometown) {
        this.userHometown = userHometown;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    public String getUserPicGroup() {
        return userPicGroup;
    }

    public void setUserPicGroup(String userPicGroup) {
        this.userPicGroup = userPicGroup;
    }

    public String getUserPicFileName() {
        return userPicFileName;
    }

    public void setUserPicFileName(String userPicFileName) {
        this.userPicFileName = userPicFileName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
