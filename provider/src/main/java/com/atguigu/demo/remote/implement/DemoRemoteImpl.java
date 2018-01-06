/*
 * Copyright (c) 2017, Xuliang Huang. All rights reserved.
 */

package com.atguigu.demo.remote.implement;

import com.atguigu.demo.interfaces.DemoRemoteInterface;
import com.atguigu.demo.pojo.Detail;
import com.atguigu.demo.pojo.DubboResult;
import com.atguigu.demo.pojo.User;
import com.atguigu.demo.service.UserService;
import com.atguigu.demo.utils.GsonUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class DemoRemoteImpl implements DemoRemoteInterface {

    @Autowired
    private UserService userService;

    @Autowired
    private SolrServer solrServer;

    @Override
    public String getMessageForTest(String param) {
        return "Hello, " + param;
    }

    @Override
    public DubboResult doSearchRemote(String keywords) {
        //1.创建SolrQuery对象
        SolrQuery solrQuery = new SolrQuery(keywords);

        //2.设置必要的参数
        solrQuery.set("df", "demo_keywords");
        solrQuery.setHighlight(true);
        solrQuery.setHighlightSimplePre("<span style='color:red'>");
        solrQuery.setHighlightSimplePost("</span>");
        solrQuery.addHighlightField("user_nick");
        solrQuery.addHighlightField("user_gender");
        solrQuery.addHighlightField("user_job");
        solrQuery.addHighlightField("user_hometown");

        //创建一个封装最终结果的Map
        HashMap<String, Map<String, Object>> finalResultMap = new HashMap<>();

        //3.执行查询
        try {
            QueryResponse response = solrServer.query(solrQuery);

            //4.获取常规数据
            SolrDocumentList results = response.getResults();

            //5.获取高亮数据
            Map<String, Map<String, List<String>>> highlightTotalMap = response.getHighlighting();

            //6.糅合：使用高亮的字段值替换对应的未高亮的字段值
            //①将results存入finalResultMap
            for (SolrDocument solrDocument : results) {

                String docId = solrDocument.getFieldValue("id") + "";

                Map<String, Object> fieldMap = new HashMap<>();

                Iterator<String> iterator = solrDocument.getFieldNames().iterator();
                while (iterator.hasNext()) {
                    String fieldName = (String) iterator.next();

                    if ("_version_".equals(fieldName)) {
                        continue;
                    }

                    Object fieldValue = solrDocument.getFieldValue(fieldName);

                    fieldMap.put(fieldName, fieldValue);

                }

                finalResultMap.put(docId, fieldMap);

            }

            //②使用highlightTotalMap中的值对finalResultMap中对应的值进行替换
            //Map<文档id, Map<字段名, List<带有高亮效果的字段值>>>
            Set<Map.Entry<String, Map<String, List<String>>>> entrySet = highlightTotalMap.entrySet();
            for (Map.Entry<String, Map<String, List<String>>> entry : entrySet) {
                String docId = entry.getKey();

                Map<String, Object> commonFieldMap = finalResultMap.get(docId);

                Map<String, List<String>> highLightFieldMap = entry.getValue();
                Set<String> keySet = highLightFieldMap.keySet();
                for (String fieldName : keySet) {

                    List<String> highLightFieldValueList = highLightFieldMap.get(fieldName);
                    if (highLightFieldValueList == null || highLightFieldValueList.size() == 0) {
                        continue;
                    }

                    String highLightFieldValue = highLightFieldValueList.get(0);

                    commonFieldMap.put(fieldName, highLightFieldValue);
                }
            }

        } catch (SolrServerException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        //为了便于通过Dubbo在网络上传输，转换为JSON字符串
        String fianlResultJson = GsonUtils.convertPojoOrMapToJson(finalResultMap);

        return new DubboResult(DubboResult.OK, fianlResultJson);
    }

    @Override
    public DubboResult doRegistRemote(User user) {

        String userName = user.getUserName();

        boolean exists = userService.checkUserNameExists(userName);

        if (exists) {

            return new DubboResult(DubboResult.FAILED, null);

        } else {

            userService.saveUser(user);

            return new DubboResult(DubboResult.OK, null);

        }

    }

    @Override
    public DubboResult doLoginRemote(User user) {

        User loginUser = userService.getLoginUser(user);

        if (loginUser == null) {
            return new DubboResult(DubboResult.FAILED, null);
        } else {
            return new DubboResult(DubboResult.OK, loginUser);
        }

    }

    @Override
    public DubboResult getDetailRemote(Integer userId) {

        //1.尝试查询Detail对象
        Detail detail = userService.getDetail(userId);

        //2.判断detail对象是否为null
        if (detail == null) {

            return new DubboResult(DubboResult.FAILED, null);

        } else {

            return new DubboResult(DubboResult.OK, detail);

        }

    }

    @Override
    public DubboResult modifyDetailRemote(Detail detail) {

        try {
            userService.saveOrUpdateDetail(detail);
        } catch (Exception e) {
            e.printStackTrace();
            return new DubboResult(DubboResult.FAILED, null);
        }

        //将Detail中的数据封装到SolrInputDocument中并添加到Solr服务器中
        //1.创建SolrInputDocument对象
        SolrInputDocument document = new SolrInputDocument();

        //2.向document对象中存入Detail实体类中封装的数据
        document.addField("id", detail.getUserId());
        document.addField("user_nick", detail.getUserNick());
        document.addField("user_gender", (detail.getUserGender() == 0) ? "男" : "女");
        document.addField("user_job", detail.getUserJob());
        document.addField("user_hometown", detail.getUserHometown());
        document.addField("user_desc", detail.getUserDesc());
        document.addField("user_pic_group", detail.getUserPicGroup());
        document.addField("user_pic_file_name", detail.getUserPicFileName());

        //3.执行文档的添加操作
        try {
            solrServer.add(document);
            //4.提交
            solrServer.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }


        return new DubboResult(DubboResult.OK, null);
    }

}
