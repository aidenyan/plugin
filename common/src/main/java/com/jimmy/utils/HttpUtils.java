package com.jimmy.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class HttpUtils {
    private HttpUtils() {
    }

    /**
     * http以及https的请求，采用key=value的方式的请求
     *
     * @param url          完整的URL地址
     * @param parameterMap 请求的参数
     * @param headerMap    请求的头部信息
     * @return 返回的结果
     */
    public static String post(String url, Map<String, Object> parameterMap, Map<String, Object> headerMap) {
        Assert.notBlank(url);
        String siteUrl = org.apache.commons.lang.StringUtils.lowerCase(url);
        if (siteUrl.startsWith("https")) {
            return postSSL(siteUrl, parameterMap, headerMap);
        }
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            setHeader(httpPost, headerMap);
            List<NameValuePair> nameValuePairs = convertParameter(parameterMap);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * http以及https的请求，采用key=value的方式的请求
     *
     * @param url          完整的URL地址
     * @param parameterMap 请求的参数
     * @return 返回的结果
     */
    public static String post(String url, Map<String, Object> parameterMap) {
        return post(url, parameterMap);
    }

    /**
     * 采用post对象的方式进行处理
     *
     * @param url       url
     * @param paramObj  参数对象
     * @param headerMap 头部信息
     * @return 返回的结果
     */
    public static String postObject(String url, Object paramObj, Map<String, Object> headerMap) {
        Assert.notBlank(url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            setHeader(httpPost, headerMap);
            if (null != paramObj) {
                //解决中文乱码问题
                StringEntity entity = new StringEntity(JSON.toJSONString(paramObj), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String postArray(String url, Map<String, Object> parameterMap, Map<String, Object> headerMap) {
        Assert.notBlank(url);
        String siteUrl = org.apache.commons.lang.StringUtils.lowerCase(url);
        if (siteUrl.startsWith("https")) {
            return postSSL(siteUrl, parameterMap, headerMap);
        }
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            setHeader(httpPost, headerMap);
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            if (parameterMap != null) {
                for (Map.Entry<String, Object> entry : parameterMap.entrySet()) {
                    String name = entry.getKey();
                    if (entry.getValue() instanceof String[]) {
                        String[] values = (String[]) entry.getValue();
                        for (String v : values) {
                            if (org.apache.commons.lang.StringUtils.isNotEmpty(v)) {
                                nameValuePairs.add(new BasicNameValuePair(name, v));
                            }
                        }
                    }
                    String value = ConvertUtils.convert(entry.getValue());
                    if (org.apache.commons.lang.StringUtils.isNotEmpty(name)) {
                        nameValuePairs.add(new BasicNameValuePair(name, value));
                    }
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 采用post对象的方式进行处理
     *
     * @param url      url
     * @param paramObj 参数对象
     * @return 返回的结果
     */
    public static String postObject(String url, Object paramObj) {
        return postObject(url, paramObj);
    }


    private static String postSSL(String url, Map<String, Object> parameterMap, Map<String, Object> headerMap) {
        Assert.notBlank(url);
        String result = null;
        CloseableHttpClient httpClient;
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(1000).build();
        HttpPost httpPost = null;
        try {
            SSLContext sslContext = SSLContexts.createDefault();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            httpPost = new HttpPost(url);
            setHeader(httpPost, headerMap);
            httpPost.setConfig(requestConfig);
            List<NameValuePair> nameValuePairs = convertParameter(parameterMap);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpPost.abort();
        }
        return result;
    }


    public static String get(String url, Map<String, Object> parameterMap, Map<String, Object> headerMap) {
        Assert.notBlank(url);
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            List<NameValuePair> nameValuePairs = convertParameter(parameterMap);
            HttpGet httpGet = new HttpGet(url + (org.apache.commons.lang.StringUtils.contains(url, "?") ? "&" : "?") + EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs, "UTF-8")));
            setHeader(httpGet, headerMap);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String get(String url, Map<String, Object> parameterMap) {
        return get(url, parameterMap, null);
    }
    private static void setHeader(HttpRequestBase httpRequest, Map<String, Object> headerMap) {

        if (null != headerMap && !headerMap.isEmpty()) {
            Iterator<String> headerIterator = headerMap.keySet().iterator();
            while (headerIterator.hasNext()) {
                String name = headerIterator.next();
                String value = headerMap.get(name).toString();
                httpRequest.setHeader(name, value);
            }
        }
    }

    private static List<NameValuePair> convertParameter(Map<String, Object> parameterMap) {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        if (parameterMap != null) {
            for (Map.Entry<String, Object> entry : parameterMap.entrySet()) {
                String name = entry.getKey();
                String value = ConvertUtils.convert(entry.getValue());
                if (org.apache.commons.lang.StringUtils.isNotEmpty(name)) {
                    nameValuePairs.add(new BasicNameValuePair(name, value));
                }
            }
        }
        return nameValuePairs;
    }


    public static void main(String[] arg) {

    }
}
