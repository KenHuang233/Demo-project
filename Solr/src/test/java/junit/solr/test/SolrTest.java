/*
 * Copyright (c) 2018, Xuliang Huang. All rights reserved.
 */

package junit.solr.test;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class SolrTest {
    private SolrServer server = new HttpSolrServer("http://localhost:8081/solr/collection1");

    @Test
    public void testHighLightQueryDataProcess() throws SolrServerException {

        SolrQuery solrQuery = new SolrQuery();

        String queryWord = "背叛";

        solrQuery.setQuery(queryWord);

        solrQuery.set("df", "my_keywords");

        solrQuery.setHighlight(true);

        solrQuery.setHighlightSimplePre("<span style='color:red'>");
        solrQuery.setHighlightSimplePost("</span>");

        solrQuery.addHighlightField("song_name");
        solrQuery.addHighlightField("song_singer");

        QueryResponse response = server.query(solrQuery);

        SolrDocumentList list = response.getResults();

        Map<String, Map<String, List<String>>> highlightTotalMap = response.getHighlighting();

        //★目标：把list中的数据和highlightTotalMap中的数据糅合到一起，能够作为页面上最终显示的数据结果
        //Map<文档id, Map<字段名,字段值>>
        //字段值：首先应该是未加高亮的输入，如果有高亮数据则使用高亮数据覆盖，否则保持原有未高亮的数据
        Map<String, Map<String, Object>> finalResultMap = new HashMap<>();

        //1.把常规未加高亮的数据存入finalResultMap
        for (SolrDocument solrDocument : list) {
            //获取当前文档的id
            String docId = solrDocument.getFieldValue("id") + "";

            //创建一个Map用来存放所有的字段名值对
            Map<String, Object> fieldMap = new HashMap<>();

            //获取所有字段名
            Collection<String> fieldNames = solrDocument.getFieldNames();

            //把文档中所有字段的名和值存入fieldMap
            for (String fieldName : fieldNames) {

                if ("_version_".equals(fieldName)) {
                    continue;
                }

                Object fieldValue = solrDocument.getFieldValue(fieldName);
                fieldMap.put(fieldName, fieldValue);
            }

            //把fieldMap存入finalResultMap
            finalResultMap.put(docId, fieldMap);

        }

        //2.使用highlightTotalMap把高亮字段值替换finalResultMap中未加高亮的值
        //遍历highlightTotalMap
        //Map<文档id,Map<字段名,List<添加了高亮效果的字段值>>>
        Set<String> keySet = highlightTotalMap.keySet();
        for (String docId : keySet) {

            //根据docId从highlightTotalMap中取出高亮的具体数据
            //Map<字段名,List<添加了高亮效果的字段值>>
            Map<String, List<String>> hightLighFieldMap = highlightTotalMap.get(docId);
            Set<Map.Entry<String, List<String>>> entrySet = hightLighFieldMap.entrySet();
            for (Map.Entry<String, List<String>> entry : entrySet) {
                String fieldName = entry.getKey();
                List<String> fieldValueList = entry.getValue();
                if (fieldValueList == null || fieldValueList.size() == 0) {
                    continue;
                }
                String fieldValue = fieldValueList.get(0);

                //根据docId从finalResultMap取出对应的字段Map
                Map<String, Object> fieldMap = finalResultMap.get(docId);

                //使用带有高亮效果的数据替换fieldMap中没有加高亮效果的原始数据
                fieldMap.put(fieldName, fieldValue);
            }

        }

        Collection<Map<String, Object>> values = finalResultMap.values();
        for (Map<String, Object> map : values) {
            Set<Map.Entry<String, Object>> entrySet = map.entrySet();
            for (Map.Entry<String, Object> entry : entrySet) {
                String key = entry.getKey();
                Object value = entry.getValue();
                System.out.println(key + "=" + value);
            }
            System.out.println();
        }
    }

    @Test
    public void testHighLightQuery() throws SolrServerException {

        SolrQuery solrQuery = new SolrQuery();

        String queryWord = "背叛";

        solrQuery.setQuery(queryWord);

        solrQuery.set("df", "my_keywords");

        //★为了得到高亮数据进行相关设置
        //1.开启高亮功能
        solrQuery.setHighlight(true);

        //2.设置高亮部分所添加的前缀标记和后缀标记
        solrQuery.setHighlightSimplePre("<span style='color:red'>");
        solrQuery.setHighlightSimplePost("</span>");

        //3.将需要进行高亮显示的字段添加进来
        solrQuery.addHighlightField("song_name");
        solrQuery.addHighlightField("song_singer");

        QueryResponse response = server.query(solrQuery);

        //★获取未加高亮效果的常规查询结果数据
        SolrDocumentList list = response.getResults();

        System.out.println("查询结果数量：" + list.getNumFound());

        for (SolrDocument solrDocument : list) {

            Object songName = solrDocument.getFieldValue("song_name");
            System.out.println("songName=" + songName);

            Object songSinger = solrDocument.getFieldValue("song_singer");
            System.out.println("songSinger=" + songSinger);

            Object songLyric = solrDocument.getFieldValue("song_lyric");
            System.out.println("songLyric=" + songLyric);

            Object songPic = solrDocument.getFieldValue("song_pic");
            System.out.println("songPic=" + songPic);

            System.out.println();
        }

        System.out.println("==============封哥线================");

        //★获取加了高亮效果的结果数据
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

		/*highlighting.entrySet();//获取entry构成的Set集合，entry是一个单独的键值对
		highlighting.keySet();//获取所有key构成的Set集合，可以进一步使用每一个key去获取对应的value
		highlighting.values();//获取所有value构成的Collection集合，但是无法反过来获取对应的key*/

        Set<String> keySet = highlighting.keySet();
        for (String key : keySet) {

            Map<String, List<String>> map = highlighting.get(key);
            System.out.println("当前文档id=" + key);

            if (map == null || map.size() == 0) {
                continue;
            }

            Set<Map.Entry<String, List<String>>> entrySet = map.entrySet();
            for (Map.Entry<String, List<String>> entry : entrySet) {
                String fieldName = entry.getKey();
                List<String> fieldValueList = entry.getValue();
                if (fieldValueList == null || fieldValueList.size() == 0) {
                    continue;
                }
                String fieldValue = fieldValueList.get(0);
                System.out.println(fieldName + "=" + fieldValue);
            }

            System.out.println();
        }

    }

    @Test
    public void testMultiFieldQuery() throws SolrServerException {

        //1.创建SolrQuery对象
        SolrQuery solrQuery = new SolrQuery();

        //2.声明一个关键词字符串
        String queryWord = "背叛";

        //3.给SolrQuery对象设置查询关键词
        solrQuery.setQuery(queryWord);

        //4.设置要查询的字段
        //查询的默认字段：default field
        solrQuery.set("df", "my_keywords");

        //5.执行查询
        QueryResponse response = server.query(solrQuery);

        //6.解析查询结果
        SolrDocumentList list = response.getResults();

        System.out.println("查询结果数量：" + list.getNumFound());

        for (SolrDocument solrDocument : list) {

            Object songName = solrDocument.getFieldValue("song_name");
            System.out.println("songName=" + songName);

            Object songSinger = solrDocument.getFieldValue("song_singer");
            System.out.println("songSinger=" + songSinger);

            Object songLyric = solrDocument.getFieldValue("song_lyric");
            System.out.println("songLyric=" + songLyric);

            Object songPic = solrDocument.getFieldValue("song_pic");
            System.out.println("songPic=" + songPic);

            System.out.println();
        }

    }

    @Test
    public void testSimpleQuery() throws SolrServerException {

        SolrQuery query = new SolrQuery();

        query.setQuery("song_name:怎样");
        query.set("fl", "song_name,song_singer");

        QueryResponse response = server.query(query);

        SolrDocumentList results = response.getResults();

        for (SolrDocument solrDocument : results) {
            Object id = solrDocument.getFieldValue("id");
            System.out.println("id=" + id);

            Object songName = solrDocument.getFieldValue("song_name");
            System.out.println("songName=" + songName);

            Object songSinger = solrDocument.getFieldValue("song_singer");
            System.out.println("songSinger=" + songSinger);

            Object songPic = solrDocument.getFieldValue("song_pic");
            System.out.println("songPic=" + songPic);

            Object songLyric = solrDocument.getFieldValue("song_lyric");
            System.out.println("songLyric=" + songLyric);

            System.out.println();
        }

    }

    @Test
    public void testDeleteById() throws SolrServerException, IOException {
        server.deleteById("99");
        server.commit();
    }

    @Test
    public void testDeleteByCondition() throws SolrServerException, IOException {
        UpdateResponse response = server.deleteByQuery("song_name:AAA");
        System.out.println(response);
        server.commit();
    }

    @Test
    public void testAddDoc() throws SolrServerException, IOException {
        SolrInputDocument document = new SolrInputDocument();

        document.addField("id", "99");
        document.addField("song_name", "TTT");
        document.addField("song_singer", "PPP");
        document.addField("song_lyric", "MEEET");
        document.addField("song_pic", "http://qukufile2.qianqian.com/data2/pic/246585831/246585831.jpg");

        UpdateResponse response = server.add(document);
        System.out.println(response);

        server.commit();
    }

}
