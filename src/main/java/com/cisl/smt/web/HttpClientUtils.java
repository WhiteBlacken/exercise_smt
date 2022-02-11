package com.cisl.smt.web;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpClientUtils {
    /**
     * 发起GET请求
     * @param url
     * @return
     * @throws IOException
     */
    public static JSONObject doGet(String url) throws IOException {
        JSONObject jsonObject = null;
        CloseableHttpClient client = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            String result = EntityUtils.toString(entity, "UTF-8");
            jsonObject = JSONObject.parseObject(result);
        }

        httpGet.releaseConnection();
        return jsonObject;
    }

    public static JSONObject doPost(String url, JSONObject paraObj) throws IOException {
        JSONObject jsonObject = null;
        CloseableHttpClient client = HttpClients.createDefault();

        // 发送请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Origin", "pass");
        if(paraObj != null) {
            StringEntity paraEntity = new StringEntity(paraObj.toString(), StandardCharsets.UTF_8);
            paraEntity.setContentEncoding("UTF-8");
            paraEntity.setContentType("application/json");  // 发送Json格式的数据请求
            httpPost.setEntity(paraEntity);
        }

        // 接收返回
        HttpResponse response = client.execute(httpPost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity, "UTF-8");
            jsonObject = JSONObject.parseObject(result);
        }

        httpPost.releaseConnection();
        return jsonObject;
    }
}
