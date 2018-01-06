/*
 * Copyright (c) 2017, Xuliang Huang. All rights reserved.
 */

package com.atguigu.demo.handlers;

import com.atguigu.demo.interfaces.DemoRemoteInterface;
import com.atguigu.demo.pojo.Detail;
import com.atguigu.demo.pojo.DubboResult;
import com.atguigu.demo.pojo.User;
import com.atguigu.demo.utils.FastDFSUtils;
import com.atguigu.demo.utils.GsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class DemoHandler {

    @Autowired
    private DemoRemoteInterface demoRemoteInterface;

    @RequestMapping("/demo/search")
    public String search(@RequestParam("keywords") String keywords, Map<String, Object> map) {
        DubboResult result = demoRemoteInterface.doSearchRemote(keywords);
        String searchResult = (String) result.getEntity();

        //把结果字符串还原为HashMap
        HashMap mapResult = GsonUtils.convertJsonToPojoOrMap(searchResult, HashMap.class);
        map.put("searchResult", mapResult);

        return "demo_search_result";
    }

    @RequestMapping("/demo/modify")
    public String modify(Detail detail, @RequestParam("uploadFile") MultipartFile uploadFile, Map<String, Object> map) throws IOException {

        boolean empty = uploadFile.isEmpty();

        //如果empty为真说明用户没有上传文件，如果为假说明用户上传的了文件
        if (!empty) {
            byte[] bytes = uploadFile.getBytes();
            String originalFilename = uploadFile.getOriginalFilename();

            String[] result = FastDFSUtils.doUpload(bytes, originalFilename);

            String groupName = result[0];
            String fileName = result[1];

            detail.setUserPicGroup(groupName);
            detail.setUserPicFileName(fileName);
        }

        DubboResult result = demoRemoteInterface.modifyDetailRemote(detail);

        String operationResult = result.getOperationResult();

        if (DubboResult.FAILED.equals(operationResult)) {
            map.put("detail", detail);
            map.put("message", "操作失败！请重新保存！");
            return "demo-modify";
        }

        return "redirect:/index.jsp";
    }

    @RequestMapping("/demo/toModifyPage/{userId}")
    public String toModifyPage(@PathVariable("userId") Integer userId, Map<String, Object> map) {

        DubboResult dubboResult = demoRemoteInterface.getDetailRemote(userId);

        String operationResult = dubboResult.getOperationResult();

        Detail detail = null;

        if (DubboResult.OK.equals(operationResult)) {
            detail = (Detail) dubboResult.getEntity();
        }

        if (DubboResult.FAILED.equals(operationResult)) {

            //由于下一个要前往的页面上要使用SpringMVC的表单标签
            //而表单标签要求从请求域中取出的实体类对象不能为null，所以这里创建Detail对象
            detail = new Detail();

            //为了能够向数据库表中插入完整的数据，这里设置userId
            detail.setUserId(userId);
        }

        map.put("detail", detail);

        return "demo-modify";
    }

    @RequestMapping(value = {"/demo/center/{userId}"})
    public String center(@PathVariable("userId") Integer userId, Map<String, Object> map) {
        DubboResult result = demoRemoteInterface.getDetailRemote(userId);

        String operationResult = result.getOperationResult();

        Detail detail = null;

        if (DubboResult.OK.equals(operationResult)) {
            detail = (Detail) result.getEntity();
        }

        map.put("detail", detail);

        return "demo-detail";
    }

    @RequestMapping(value = {"/demo/testUpload"})
    public String testUpload(@RequestParam("uploadFile") MultipartFile uploadFile, Map<String, Object> map) throws IOException {
        byte[] bytes = uploadFile.getBytes();
        String originalFilename = uploadFile.getOriginalFilename();
        String[] result = FastDFSUtils.doUpload(bytes, originalFilename);

        System.out.println("group：" + result[0]);
        System.out.println("remote file name：" + result[1]);

        map.put("groupName", result[0]);
        map.put("fileName", result[1]);

        return "test-upload";
    }

    @RequestMapping("/demo/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/index.jsp";
    }

    @RequestMapping("/demo/login")
    public String login(User user, HttpSession session, Map<String, Object> map) {

        DubboResult result = demoRemoteInterface.doLoginRemote(user);
        if (DubboResult.OK.equals(result.getOperationResult())) {
            User loginUser = (User) result.getEntity();
            session.setAttribute("loginUser", loginUser);
            return "redirect:/index.jsp";
        } else {
            map.put("message", "用户名密码不正确，请重新输入！");
            return "demo-login";
        }

    }

    @RequestMapping("/demo/regist")
    public String regist(User user, Map<String, Object> map) {

        DubboResult result = demoRemoteInterface.doRegistRemote(user);

        String operationResult = result.getOperationResult();

        if (DubboResult.OK.equals(operationResult)) {

            return "redirect:/demo/toLoginPage";

        } else if (DubboResult.FAILED.equals(operationResult)) {
            map.put("message", "用户名已存在，请重新注册！");
            return "demo-regist";
        }

        return "redirect:/index.jsp";
    }

    @RequestMapping("/demo/test")
    public String test(Map<String, Object> map) {

        String result = demoRemoteInterface.getMessageForTest("小刚刚");
        map.put("result", result);

        return "demo-result";
    }
}
